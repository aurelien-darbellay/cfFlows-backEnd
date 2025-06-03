package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.interfaces.UserMapableToDto;
import s05t02.interactiveCV.service.entities.UserService;

import static s05t02.interactiveCV.globalVariables.ApiPaths.ADMIN_BASE_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping(ADMIN_BASE_PATH)
public class AdminController {

    private final UserService userService;

    @GetMapping
    public Flux<DashBoardDto> getAdminDashboard() {
      return userService.getAllUser()
              .map(UserMapableToDto::mapToDto);
    }

}
