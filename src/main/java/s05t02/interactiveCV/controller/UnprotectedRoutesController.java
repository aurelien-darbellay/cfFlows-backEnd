package s05t02.interactiveCV.controller;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.RegistrationRequestDto;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.service.entities.UserService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class UnprotectedRoutesController {

    private final UserService userService;
    private final PublicViewService publicViewService;
    private final PasswordEncoder encoder;
    private static final Logger log = LoggerFactory.getLogger(UnprotectedRoutesController.class);

    @GetMapping("/csrf")
    public Mono<Map<String, String>> csrf(ServerWebExchange exchange) {
        return exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName())
                .flatMap(token -> Mono.just(Map.of("token", token.getToken())));
    }

    @PostMapping("/register")
    Mono<ResponseEntity<Void>> registerNewUser(@RequestBody RegistrationRequestDto request) {
        return userService.saveUser(User.builder()
                        .username(request.getUsername())
                        .hashedPassword(encoder.encode(request.getPassword()))
                        .firstname(request.getFirstname())
                        .lastname(request.getLastname())
                        .build())
                .map(user -> ResponseEntity.status(HttpStatus.FOUND).header("Location", ApiPaths.USER_BASE_PATH.replace("{username}", request.getUsername())).build());
    }

    @GetMapping("/public-views")
    Mono<PublicView> getPublicViewById(@RequestParam String id) {
        return publicViewService.getPublicViewById(id)
                .doOnSuccess(publicView -> log.atDebug().log("Retrieved public view: {}", publicView.toString()));
    }
}
