package s05t02.interactiveCV.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.Role;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class SecurityConfigFullyIntegratedTest {

    static final Logger log = LoggerFactory.getLogger(SecurityConfigFullyIntegratedTest.class);

    static final String USERNAME_1 = "user1";
    static final String PASSWORD_1 = "pass1";
    static final Role ROLE_1 = Role.USER;
    static final String USERNAME_2 = "user2";
    static final String PASSWORD_2 = "pass2";
    static final Role ROLE_2 = Role.USER;
    static final String USERNAME_3 = "user3";
    static final String PASSWORD_3 = "pass3";
    static final Role ROLE_3 = Role.ADMIN;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebTestClient client;

    @Autowired
    private JwtDecoder jwtDecoder;


    @BeforeEach
    void setUp() {
        User user1 = userService.saveUser(User.builder().id(USERNAME_1).userName(USERNAME_1).hashedPassword(passwordEncoder.encode(PASSWORD_1)).role(ROLE_1).build()).block();
        User user2 = userService.saveUser(User.builder().id(USERNAME_2).userName(USERNAME_2).hashedPassword(passwordEncoder.encode(PASSWORD_2)).role(ROLE_2).build()).block();
        User user3 = userService.saveUser(User.builder().id(USERNAME_3).userName(USERNAME_3).hashedPassword(passwordEncoder.encode(PASSWORD_3)).role(ROLE_3).build()).block();
    }

    @Test
    void setUpWorks() {
        log.info("Check test DB");
    }

    @Test
    void whenFormLoginWithValidCredentials_thenRedirectsAndSetLocationHeader() {
        client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", USERNAME_1)
                        .with("password", PASSWORD_1))
                .exchange()
                .expectStatus().isFound()
                .expectHeader().exists(HttpHeaders.LOCATION);
    }

    @Test
    void cookieWithJwtGeneratedUponSuccessfulLogin() {
        client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", USERNAME_2)
                        .with("password", PASSWORD_2))
                .exchange()
                .expectCookie().exists("jwt")
                .expectCookie().httpOnly("jwt", true);
    }

    @Test
    void cookieReturnedAfterSuccessfulLoginPermitsAuthentication() {
        FluxExchangeResult<byte[]> result = client.mutateWith(csrf())
                .post()
                .uri(ApiPaths.LOGIN_PATH)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters
                        .fromFormData("username", USERNAME_2)
                        .with("password", PASSWORD_2))
                .exchange()
                .returnResult(byte[].class);


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
                        .fromFormData("username", USERNAME_3)
                        .with("password", PASSWORD_3))
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

        assertEquals(USERNAME_3, username);
        assertLinesMatch(List.of(ROLE_3.getAuthorityName()), authorities);
    }

}
