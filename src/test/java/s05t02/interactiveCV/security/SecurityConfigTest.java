package s05t02.interactiveCV.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.Role;
import s05t02.interactiveCV.service.security.MyUserDetailsService;
import s05t02.interactiveCV.service.security.jwt.JwtUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class SecurityConfigTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    void setUp() {
        when(myUserDetailsService.findByUsername("user")).thenReturn(Mono.just(User.builder().username("user").password(passwordEncoder.encode("secret")).roles("USER").build()));
        when(myUserDetailsService.findByUsername("")).thenReturn(Mono.empty());
    }

    @Test
    void whenNoCredentials_thenUnAuthorized() {
        client.get()
                .uri(ApiPaths.PROTECTED_BASE_PATH + "/protected")      // ← your secured URI
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenPreflightRequestWithUnauthorizedOrigin_thenNoCorsHeaders() {
        client.options()
                .uri(ApiPaths.PROTECTED_BASE_PATH + "/protected")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().doesNotExist("Access-Control-Allow-Origin");
    }

    @Test
    void whenPreflightRequestWithAuthorizedOrigin_thenCorsHeaders() {
        client.options()
                .uri("http://hostname-ignored:666" + ApiPaths.PROTECTED_BASE_PATH + "/protected")
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "GET")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:5173")
                .expectHeader().valueEquals("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
    }

    @Test
// authenticate so we’re only testing CSRF, not authentication
    void whenPostWithoutCsrfToken_thenReturnsForbidden() {
        client.post()
                .uri("http://hostname-ignored:666" + ApiPaths.PROTECTED_BASE_PATH + "/protected")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostingWithCsrfTokenOnWithoutAuthentication_thenUnAuthorized() {
        client.mutateWith(csrf())
                .post()
                .uri("http://hostname-ignored:666" + ApiPaths.PROTECTED_BASE_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenFormLoginWithValidCredentials_thenRedirectsAndSetLocationHeader() {
        client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", "user")
                        .with("password", "secret"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void whenValidJwtIsSubmittedForUnexistingRessource_thenUserIdentifiedAndWeGet404() {
        Jwt jwt = jwtUtils.createJwt("alice", List.of(Role.USER.getAuthorityName()));
        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.PROTECTED_BASE_PATH + "/my-cv-is-not-found")
                .cookie("jwt", jwt.getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void authenticationFailsWhenTokenExpired() {
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject("aure")
                .expiresAt(Instant.now().minus(Duration.ofHours(1)))
                .build();
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(headers, claimsSet));
        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.PROTECTED_BASE_PATH)
                .cookie("jwt", jwt.getTokenValue())
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void cookieWithJwtGeneratedUponSuccessfulLogin() {
        client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", "user")
                        .with("password", "secret"))
                .exchange()
                .expectCookie().exists("jwt")
                .expectCookie().httpOnly("jwt", true);
    }

    @Test
    void cookieReturnedAfterSuccessulLoginPermitsAuthentication() {
        FluxExchangeResult<byte[]> result = client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", "user")
                        .with("password", "secret"))
                .exchange()
                .returnResult(byte[].class);

        @SuppressWarnings("ConstantConditions")
        String jwt = result.getResponseCookies()
                .getFirst("jwt")
                .getValue();

        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.PROTECTED_BASE_PATH + "/my-cv-is-not-found")
                .cookie("jwt", jwt)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void JwtReturnedContainsProperUsernameAndClaims() {
        FluxExchangeResult<byte[]> result = client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", "user")
                        .with("password", "secret"))
                .exchange()
                .returnResult(byte[].class);

        @SuppressWarnings("ConstantConditions")
        String jwtString = result.getResponseCookies()
                .getFirst("jwt")
                .getValue();

        Jwt jwt = jwtDecoder.decode(jwtString);
        String username = (String) jwt.getClaims().get("sub");
        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) jwt.getClaims().get("roles");

        assertEquals("user", username);
        assertLinesMatch(List.of("ROLE_USER"), authorities);
    }

    @Test
    void AuthorizedWhenTryingToAccessMySpace() {
        Jwt jwt = jwtUtils.createJwt("unexistingUser", List.of(Role.USER.getAuthorityName()));
        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.USER_DASHBOARD_PATH.replace("{username}", "unexistingUser"))
                .cookie("jwt", jwt.getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void UnAuthorizedWhenTryingAccessAdminWithoutAuthority() {
        Jwt jwt = jwtUtils.createJwt("alice", List.of(Role.USER.getAuthorityName()));
        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .cookie("jwt", jwt.getTokenValue())
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void AuthorizedWhenTryingAccessAdminWithAuthority() {
        Jwt jwt = jwtUtils.createJwt("alice", List.of(Role.ADMIN.getAuthorityName()));
        client.mutateWith(csrf())
                .get()
                .uri(ApiPaths.ADMIN_BASE_PATH + "/inexistent-ressource")
                .cookie("jwt", jwt.getTokenValue())
                .exchange()
                .expectStatus().isNotFound();
    }
}
