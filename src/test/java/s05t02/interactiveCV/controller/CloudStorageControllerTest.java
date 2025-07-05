package s05t02.interactiveCV.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.service.cloud.CloudStorageService;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest
@AutoConfigureWebTestClient
public class CloudStorageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CloudStorageService cloudStorageService;

    @MockitoBean
    private JwtDecoder jwtDecoder; // Needed if your security requires JWT validation

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getSignature_ShouldReturnSignedData() {
        // Mock service response
        Map<String, Object> mockResponse = Map.of(
                "signature", "abc123",
                "timestamp", "2023-01-01"
        );


        when(cloudStorageService.authenticateUpload(anyString(), anyString()))
                .thenReturn(Mono.just(mockResponse));

        Map<String, Object> requestBody = Map.of("fileName", "test.jpg");

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ApiPaths.CLOUD_STORAGE_PATH + "/signature", "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.signature").isEqualTo("abc123")
                .jsonPath("$.timestamp").exists();
    }

    @Test
    void getSignature_ShouldReturnUnauthorizedWithoutAuth() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri(ApiPaths.CLOUD_STORAGE_PATH + "/signature", "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("fileName", "test.jpg"))
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getSignature_ShouldReturnBadRequestForInvalidInput() {
        webTestClient.mutateWith(csrf())
                .post()
                .uri(ApiPaths.CLOUD_STORAGE_PATH + "/signature", "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of()) // Empty body
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getSignature_ShouldHandleServiceErrors() {
        when(cloudStorageService.authenticateUpload(anyString(), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Cloud service error")));

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ApiPaths.CLOUD_STORAGE_PATH + "/signature", "testuser")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("fileName", "test.jpg"))
                .exchange()
                .expectStatus().is5xxServerError();
    }
}