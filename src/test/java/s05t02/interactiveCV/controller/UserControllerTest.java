package s05t02.interactiveCV.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.DocFacade;
import s05t02.interactiveCV.dto.UserUpdateRequestDto;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.service.entities.UserService;
import s05t02.interactiveCV.service.security.jwt.JwtCookieSuccessHandler;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@SpringBootTest
@AutoConfigureWebTestClient
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtCookieSuccessHandler successHandler;

    @MockitoBean
    private ReactiveAuthenticationManager authManager;

    private final String testUsername = "testuser";
    private final DashBoardDto mockDashboard = new DashBoardDto(testUsername, "user", "user", "USER", List.of(new DocFacade("123", "Lol")));

    @Test
    void updateUserDetails_ShouldWorkWithValidRequest() {
        UserUpdateRequestDto request = new UserUpdateRequestDto(testUsername, "newPassword", "New", "Name", "USER");

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(testUsername, request.getPassword());

        Jwt jwt = Jwt.withTokenValue("mock-jwt-token")
                .header("alg", "none")
                .claim("sub", "John")
                .claim("roles", List.of("ROLE_USER"))
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(userService.updateUser(any(UserUpdateRequestDto.class)))
                .thenReturn(Mono.just(mockDashboard));
        when(authManager.authenticate(any()))
                .thenReturn(Mono.just(mockAuth));
        when(successHandler.createJwtCookie(any(), any()))
                .thenReturn(jwt);

        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser(testUsername))
                .post()
                .uri(USER_BASE_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DashBoardDto.class)
                .isEqualTo(mockDashboard);
    }

    @Test
    void updateUserDetails_ShouldValidateInput() {
        // Test empty firstname
        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser(testUsername))
                .post()
                .uri(USER_BASE_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequestDto(testUsername, "", "", "Name", "USER"))
                .exchange()
                .expectStatus().isBadRequest();

        // Test invalid password
        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser(testUsername))
                .post()
                .uri(USER_BASE_PATH, testUsername)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequestDto("", "pass", "first", "last", "USER"))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getUserDashboard_ShouldReturnDashboard() {
        when(userService.getUserByUserName(testUsername))
                .thenReturn(Mono.just(User.builder().username(testUsername).hashedPassword("pass").firstname("Test").lastname("User").build()));

        webTestClient.mutateWith(mockUser(testUsername))
                .get()
                .uri(USER_BASE_PATH + USER_DASHBOARD_REL)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DashBoardDto.class)
                .value(dto -> {
                    assert dto.username().equals(testUsername);
                    assert dto.firstname().equals("Test");
                });
    }

    @Test
    void getUserDashboard_ShouldReturnNotFoundForInvalidUser() {
        when(userService.getUserByUserName("nonexistent"))
                .thenReturn(Mono.error(new EntityNotFoundException("nonexistent")));

        webTestClient.mutateWith(mockUser("nonexistent"))
                .get()
                .uri(USER_BASE_PATH + USER_DASHBOARD_REL)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteUser_ShouldWorkWhenAuthorized() {
        when(userService.deleteUserByUserName(testUsername))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(csrf())
                .mutateWith(mockUser(testUsername))
                .post()
                .uri(USER_BASE_PATH + USER_DELETE_REL)
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void updateUserDetails_ShouldRequireAuthentication() {
        webTestClient.mutateWith(csrf())
                .post() // No authentication
                .uri(USER_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserUpdateRequestDto(testUsername, "pass", "first", "last", "USER"))
                .exchange()
                .expectStatus().isFound();
    }
}