package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.interfaces.UserMapableToDto;
import s05t02.interactiveCV.service.entities.UserService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER_BASE_PATH)
public class UserController {
    private final UserService userService;

    @GetMapping(USER_DASHBOARD_REL)
    Mono<DashBoardDto> getUserDashBoard(@PathVariable("username") String username) {
        return userService.getUserByUserName(username)
                .map(UserMapableToDto::mapToDto);
    }

    @PostMapping(USER_DELETE_REL)
    Mono<Void> deleteUserByUsername(@PathVariable("username") String username){
        return userService.deleteUserByUserName(username);
    }
}
