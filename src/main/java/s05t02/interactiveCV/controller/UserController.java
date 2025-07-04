package s05t02.interactiveCV.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.PublicViewsDashboardDto;
import s05t02.interactiveCV.dto.UserUpdateRequestDto;
import s05t02.interactiveCV.dto.interfaces.PublicViewMapableToDto;
import s05t02.interactiveCV.dto.interfaces.UserMapableToDto;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.service.entities.UserService;
import s05t02.interactiveCV.service.security.jwt.JwtCookieSuccessHandler;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_BASE_PATH)
public class UserController {
    private final UserService userService;
    private final PublicViewService publicViewService;
    private final JwtCookieSuccessHandler successHandler;
    private final ReactiveAuthenticationManager authManager;

    @PostMapping
    Mono<DashBoardDto> updateUserDetails(ServerWebExchange exchange, @Valid @RequestBody UserUpdateRequestDto dto) {
        return userService.updateUser(dto)
                .delayUntil(dashBoardDto -> {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
                    return authManager.authenticate(token)
                            .doOnSuccess(authentication -> {
                                successHandler.createJwtCookie(exchange, authentication);
                            });
                });
    }

    @GetMapping(USER_DASHBOARD_REL)
    Mono<DashBoardDto> getUserDashBoard() {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> userService.getUserByUserName(username)
                        .map(UserMapableToDto::mapToDto));
    }

    @PostMapping(USER_DELETE_REL)
    Mono<Void> deleteUserByUsername() {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(userService::deleteUserByUserName);
    }

    @PostMapping(PV_PATH_REL)
    Mono<PublicView> createPublicView(@PathVariable String id, @RequestBody InteractiveDocument document) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> publicViewService.savePublicView(username, document, id));
    }

    @GetMapping(PVs_PATH_REL)
    Mono<PublicViewsDashboardDto> getPublicViews() {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> publicViewService.getAllPublicViewsFromUser(username)
                        .map(PublicViewMapableToDto::mapToDto)
                        .collectList()
                        .map(facades -> PublicViewsDashboardDto.of(username, facades)));
    }
}
