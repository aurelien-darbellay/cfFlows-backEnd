package s05t02.interactiveCV.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SecurityConfigTest {

    @Autowired
    private WebTestClient client;

    @Value("${app.api.base-path}")
    private String apiBasePath;

    @Test
    void whenNoCredentials_thenProtectedEndpointReturnsUnauthorized() {
        client.get()
                .uri(apiBasePath+"/protected")      // ← your secured URI
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void whenPreflightRequestWithoutCorsConfig_thenNoCorsHeaders() {
        client.options()
                .uri(apiBasePath+"/protected")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().doesNotExist("Access-Control-Allow-Origin");
    }

    @Test
    void whenPreflightRequestExpectCors_thenFailingAssertion() {
        client.options()
                .uri("http://hostname-ignored:666"+apiBasePath+"/protected")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:5173")
                .expectHeader().valueEquals("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    }

    @Test// authenticate so we’re only testing CSRF, not authentication
    void whenPostWithoutCsrfToken_andCsrfEnabled_thenReturnsForbidden() {
        client.post()
                .uri("http://hostname-ignored:666"+apiBasePath + "/protected")
                // no CSRF token header or cookie supplied
                .exchange()
                // if CSRF is enabled, this must be 403;
                // if you’ve disabled CSRF, you’ll get 200 OK and this test will fail.
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostingWithCsrfTokenOnAuthorizedPath_thenReturnsOk(){
        client.mutateWith(csrf())
                .post()
                .uri("http://hostname-ignored:666/login")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void whenFormLoginWithValidCredentials_thenRedirectsAndSetLocationHeader() {
        client.mutateWith(csrf())
                .post()
                .uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", "user")
                        .with("password", "secret"))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueEquals(HttpHeaders.LOCATION, "/");
    }
}
