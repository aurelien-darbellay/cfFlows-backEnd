package s05t02.interactiveCV.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DocumentCreationDto;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.exception.MatchingFailureException;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.InteractiveDocumentType;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.service.entities.InteractiveDocumentService;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static s05t02.interactiveCV.globalVariables.ApiPaths.DOC_PATH;

@SpringBootTest
@AutoConfigureWebTestClient
public class DocumentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private InteractiveDocumentService documentService;

    private final String testUsername = "testuser";
    private final String testDocId = "doc123";

    @Test
    @WithMockUser(username = "testuser")
    void createNewDocument_ShouldReturnCreatedDocument() {
        DocumentCreationDto request = new DocumentCreationDto(
                "My Resume",
                InteractiveDocumentType.INTERACTIVE_CV
        );

        InteractiveDocument mockDocument = InteractiveCv.builder().build();
        mockDocument.setId(testDocId);
        mockDocument.setTitle(request.getTitle());

        when(documentService.createDocumentInUser(
                testUsername,
                request.getType(),
                request.getTitle()
        )).thenReturn(Mono.just(mockDocument));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(InteractiveDocument.class)
                .value(doc -> {
                    assert doc.getId().equals(testDocId);
                    assert doc.getTitle().equals(request.getTitle());
                });
    }

    @Test
    @WithMockUser(username = "testuser")
    void createNewDocument_ShouldValidateInput() {
        // Test empty title
        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new DocumentCreationDto("hola", null))
                .exchange()
                .expectStatus().isBadRequest();

        // Test null type
        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new DocumentCreationDto("", InteractiveDocumentType.INTERACTIVE_CV))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDocumentById_ShouldReturnDocument() {
        InteractiveDocument mockDocument = InteractiveCv.builder().build();
        mockDocument.setId(testDocId);

        when(documentService.getDocumentByIdInUser(testUsername, testDocId))
                .thenReturn(Mono.just(mockDocument));

        webTestClient.get()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL, testUsername, testDocId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InteractiveDocument.class)
                .value(doc -> {
                    assert doc.getId().equals(testDocId);
                });
    }

    @Test
    @WithMockUser(username = "testuser")
    void getDocumentById_ShouldReturnNotFoundForInvalidDoc() {
        when(documentService.getDocumentByIdInUser(testUsername, "invalid"))
                .thenReturn(Mono.error(new EntityNotFoundException("invalid")));

        webTestClient.get()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL, testUsername, "invalid")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @WithMockUser(username = "testuser")
    void updateDocument_ShouldReturnUpdatedDocument() {
        InteractiveDocument updatedDoc = InteractiveCv.builder().build();
        updatedDoc.setId(testDocId);
        updatedDoc.setTitle("Updated Title");

        when(documentService.updateDocumentInUser(testUsername, updatedDoc))
                .thenReturn(Mono.just(updatedDoc));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL, testUsername, testDocId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedDoc)
                .exchange()
                .expectStatus().isOk()
                .expectBody(InteractiveDocument.class)
                .value(doc -> {
                    assert doc.getTitle().equals("Updated Title");
                });
    }

    @Test
    @WithMockUser(username = "testuser")
    void deleteDocument_ShouldReturnNoContent() {
        when(documentService.deleteDocumentFromUser(testUsername, testDocId))
                .thenReturn(Mono.error(new MatchingFailureException(testUsername, testDocId)));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL + ApiPaths.DELETE_DOC_REL,
                        testUsername, testDocId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void allEndpoints_ShouldRequireAuthentication() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri(DOC_PATH, testUsername)
                .exchange()
                .expectStatus().isFound();

        webTestClient.get()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL, testUsername, testDocId)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @WithMockUser(username = "otheruser")
    void operations_ShouldValidateUsernameOwnership() {
        // Simulate trying to access another user's documents
        /*when(documentService.getDocumentByIdInUser(testUsername, testDocId))
                .thenReturn(Mono.error(new SecurityException("Forbidden")));*/

        webTestClient.get()
                .uri(DOC_PATH + ApiPaths.DOC_ID_REL, testUsername, testDocId)
                .exchange()
                .expectStatus().isForbidden();
    }
}