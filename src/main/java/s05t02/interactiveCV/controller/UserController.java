package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.service.entities.UserService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.USER_BASE_PATH;
import static s05t02.interactiveCV.globalVariables.ApiPaths.USER_DASHBOARD_REL;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_BASE_PATH)
public class UserController {
    private final UserService userService;

    @GetMapping(USER_DASHBOARD_REL)
    Mono<DashBoardDto> getUserDashBoard(@PathVariable String username) {
        return userService.getUserByUserName(username)
                .map(DashBoardDto::mapUserToDashBoard);
    }
}
