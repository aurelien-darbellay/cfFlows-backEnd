package s05t02.interactiveCV.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.exception.MatchingFailureException;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Contact;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Summary;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.service.entities.EntryService;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class EntryControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private EntryService entryService;

    @MockitoBean
    private JwtDecoder jwtDecoder; // Required for JWT validation

    private final String testUsername = "testuser";
    private final String testDocId = "doc123";
    private final Entry testEntry = Summary.builder().title("hola").build();
    private String validJwtToken;

    @BeforeEach
    void setUp() {
        // Setup mock JWT
        validJwtToken = "mock-jwt-token";
        Jwt jwt = Jwt.withTokenValue(validJwtToken)
                .header("alg", "none")
                .claim("sub", testUsername)
                .claim("roles", List.of("ROLE_USER"))
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(validJwtToken)).thenReturn(jwt);
        when(jwtDecoder.decode("invalid-token")).thenThrow(new JwtException("Invalid token"));
    }

    @Test
    void addEntry_ShouldReturnCreatedEntry_WhenAuthenticated() {
        when(entryService.addEntry(eq(testUsername), eq(testDocId), any(Entry.class)))
                .thenReturn(Mono.just(testEntry));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_ADD_REL, testUsername, testDocId)
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Entry.class)
                .isEqualTo(testEntry);
    }

    @Test
    void addEntry_ShouldReturnUnauthorized_WhenNoJwtCookie() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_ADD_REL, testUsername, testDocId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void addEntry_ShouldReturnForbidden_WhenInvalidJwt() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_ADD_REL, testUsername, testDocId)
                .cookie("jwt", "invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void updateEntry_ShouldReturnUpdatedEntry_WhenAuthorized() {
        Entry updatedEntry = Contact.builder().cityOfResidence("harley").build();
        when(entryService.modifyEntry(testUsername, testDocId, updatedEntry))
                .thenReturn(Mono.just(updatedEntry));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_UPDATE_REL, testUsername, testDocId)
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedEntry)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Entry.class)
                .value(e -> ((Contact) e).getCityOfResidence().equals("harley"));
    }

    @Test
    void deleteEntry_ShouldReturnNoContent_WhenSuccessful() {
        when(entryService.removeEntry(testUsername, testDocId, testEntry))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_DELETE_REL, testUsername, testDocId)
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void allEntryEndpoints_ShouldRequireCsrfProtection() {
        // Test without CSRF token
        webTestClient.post()
                .uri(ENTRY_BASE_PATH + ENTRY_ADD_REL, testUsername, testDocId)
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isForbidden();

        // Test with CSRF token
        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_ADD_REL, testUsername, testDocId)
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isCreated();
    }


    @Test
    void updateEntry_ShouldReturnNotFound_WhenDocumentMissing() {
        when(entryService.modifyEntry(testUsername, "missing-doc", testEntry))
                .thenReturn(Mono.error(new MatchingFailureException(testUsername, "missing-doc")));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ENTRY_BASE_PATH + ENTRY_UPDATE_REL, testUsername, "missing-doc")
                .cookie("jwt", validJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(testEntry)
                .exchange()
                .expectStatus().isNotFound();
    }
}