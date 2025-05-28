package s05t02.interactiveCV.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.RegistrationRequestDto;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.service.entities.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UnprotectedRoutesController {

    private final UserService userService;
    private final PasswordEncoder encoder;
    private final ServerCsrfTokenRepository csrfTokenRepository;

    @GetMapping("/csrf")
    public Mono<CsrfToken> csrf(ServerWebExchange exchange) {
        return csrfTokenRepository.loadToken(exchange)
                .switchIfEmpty(csrfTokenRepository.generateToken(exchange)
                        .flatMap(token -> csrfTokenRepository.saveToken(exchange, token).thenReturn(token)));
    }

    @PostMapping("register")
    Mono<ResponseEntity<User>> registerNewUser(@RequestBody RegistrationRequestDto request) {
        return userService.saveUser(User.builder()
                        .username(request.getUsername())
                        .hashedPassword(encoder.encode(request.getPassword()))
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .build())
                .map(user -> ResponseEntity.status(HttpStatus.FOUND).header("Location", ApiPaths.USER_BASE_PATH.replace("{username}", request.getUsername())).body(user));
    }
}
