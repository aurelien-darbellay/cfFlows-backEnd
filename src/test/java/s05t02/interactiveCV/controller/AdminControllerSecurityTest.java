package s05t02.interactiveCV.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.service.entities.UserService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureWebTestClient// Import your security configuration
public class AdminControllerSecurityTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ReactiveJwtDecoder reactiveJwtDecoder;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    private Jwt createMockJwt(boolean isAdmin) {
        Map<String, Object> claims = Map.of(
                "sub", "123",
                "roles", isAdmin ? List.of("ROLE_ADMIN") : List.of("ROLE_USER")
        );
        return Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claims(c -> c.putAll(claims))
                .build();
    }

    @Test
    void getAdminDashboard_ShouldReturnForbiddenForNonAdmin() {
        // Mock JWT for non-admin user
        Jwt jwt = createMockJwt(false);
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        webTestClient.get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .cookie("jwt", "mock-token")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void getAdminDashboard_ShouldFoundWithoutJwt() {
        webTestClient.get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    void getAdminDashboard_ShouldReturnDataForAdmin() {
        // Mock JWT for admin user
        Jwt jwt = createMockJwt(true);
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        // Mock service response
        User user = User.builder().build();
        when(userService.getAllUser()).thenReturn(Flux.just(user));

        webTestClient.get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .cookie("jwt", "mock-token")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DashBoardDto.class)
                .hasSize(1);
    }

    @Test
    void getAdminDashboard_ShouldHandleInvalidJwt() {
        when(jwtDecoder.decode(anyString())).thenThrow(new JwtException("Invalid token"));

        webTestClient.get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .cookie("jwt", "invalid-token")
                .exchange()
                .expectStatus().isFound();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
        // Alternative approach using Spring Security test support
    void getAdminDashboard_WithMockAdminUser_ShouldReturnData() {
        // Mock service response
        User user = User.builder().build();
        when(userService.getAllUser()).thenReturn(Flux.just(user));

        webTestClient.get()
                .uri(ApiPaths.ADMIN_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DashBoardDto.class)
                .hasSize(1);
    }
}