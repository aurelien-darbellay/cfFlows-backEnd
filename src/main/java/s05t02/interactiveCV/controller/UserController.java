package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.UserUpdateRequestDto;
import s05t02.interactiveCV.dto.interfaces.UserMapableToDto;
import s05t02.interactiveCV.service.entities.UserService;
import s05t02.interactiveCV.service.security.jwt.JwtCookieSuccessHandler;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_BASE_PATH)
public class UserController {
    private final UserService userService;
    private final JwtCookieSuccessHandler successHandler;
    private final ReactiveAuthenticationManager authManager;

    @PostMapping
    Mono<DashBoardDto> updateUserDetails(ServerWebExchange exchange, @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(dto)
                .delayUntil(ignored -> {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
                    return authManager.authenticate(token)
                            .doOnSuccess(authentication -> {
                                successHandler.createJwtCookie(exchange, authentication);
                            });
                });
    }

    @GetMapping(USER_DASHBOARD_REL)
    Mono<DashBoardDto> getUserDashBoard(@PathVariable("username") String username) {
        return userService.getUserByUserName(username)
                .map(UserMapableToDto::mapToDto);
    }

    @PostMapping(USER_DELETE_REL)
    Mono<Void> deleteUserByUsername(@PathVariable("username") String username) {
        return userService.deleteUserByUserName(username);
    }
}
