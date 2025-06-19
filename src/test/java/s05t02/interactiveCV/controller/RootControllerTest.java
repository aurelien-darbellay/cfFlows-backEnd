package s05t02.interactiveCV.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.RegistrationRequestDto;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.TypesConfig;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.service.entities.UserService;
import s05t02.interactiveCV.service.security.jwt.JwtCookieSuccessHandler;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication;

@SpringBootTest
@AutoConfigureWebTestClient
public class RootControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private PublicViewService publicViewService;

    @MockitoBean
    private ReactiveAuthenticationManager authManager;

    @MockitoBean
    private JwtCookieSuccessHandler successHandler;

    @MockitoBean
    private TypesConfig typesConfig;

    @Test
    void getCsrfToken_ShouldReturnToken() {
        webTestClient.get()
                .uri(ApiPaths.CSRF_TOKEN_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();
    }

    @Test
    void registerNewUser_ShouldWorkWithValidRequest() {
        RegistrationRequestDto request = new RegistrationRequestDto("newuser", "Password123!");
        request.setFirstname("John");
        request.setLastname("Doe");

        User mockUser = User.builder().build();
        mockUser.setUsername(request.getUsername());

        Jwt jwt = Jwt.withTokenValue("mock-jwt-token")
                .header("alg", "none")
                .claim("sub", "John")
                .claim("roles", List.of("ROLE_USER"))
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword());

        when(userService.saveUser(any(User.class))).thenReturn(Mono.just(mockUser));
        when(authManager.authenticate(any())).thenReturn(Mono.just(mockAuth));
        when(successHandler.createJwtCookie(any(), any())).thenReturn(jwt);

        webTestClient.mutateWith(csrf())
                .post()
                .uri(ApiPaths.REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueEquals("Location", ApiPaths.USER_DASHBOARD_PATH);
    }

    @Test
    void getPublicViewById_ShouldReturnPublicView() {
        PublicView mockView = PublicView.builder().id("test123").build();

        when(publicViewService.getPublicViewById("test123"))
                .thenReturn(Mono.just(mockView));

        webTestClient.get()
                .uri(ApiPaths.PUBLIC_VIEWS_PATH + "?id=test123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PublicView.class)
                .value(view -> view.getId().equals("test123"));
    }

    @Test
    void getTypesConfig_ShouldReturnConfig() {
        when(typesConfig.getDocumentTypes()).thenReturn(List.of("INTERACTIVE_CV"));

        when(typesConfig.getEntryTypes()).thenReturn(List.of("SUMMARY"));

        webTestClient.get()
                .uri(ApiPaths.TYPES_CONFIG_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TypesConfig.class)
                .value(config -> {
                    assertThat(config.getDocumentTypes().contains("INTERACTIVE_CV")).isTrue();
                    assertThat(config.getEntryTypes().contains("SUMMARY")).isTrue();
                });
    }

    @Test
    void registerNewUser_ShouldFailWithoutCsrf() {
        RegistrationRequestDto request = new RegistrationRequestDto("newuser", "Password123!");
        request.setFirstname("John");
        request.setLastname("Doe");
        webTestClient.post()  // No CSRF token
                .uri(ApiPaths.REGISTER_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void isAuthenticated_ShouldReturnOk_WhenValidJwtCookiePresent() {

        webTestClient
                .mutateWith(mockAuthentication(new UsernamePasswordAuthenticationToken("John", "pass123", List.of(new SimpleGrantedAuthority("ROLE_USER")))))
                .get()
                .uri(ApiPaths.AUTHENTICATION_CHECK_PATH)
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    void isAuthenticated_ShouldReturnUnauthorized_WhenNoJwtCookiePresent() {
        webTestClient.get()
                .uri(ApiPaths.AUTHENTICATION_CHECK_PATH)
                .exchange()
                .expectStatus().isUnauthorized();
    }


}