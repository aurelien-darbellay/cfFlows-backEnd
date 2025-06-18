package s05t02.interactiveCV.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.service.cloud.CloudStorageService;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.CLOUD_STORAGE_PATH)
@RequiredArgsConstructor
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

    @PostMapping("/signature")
    public Mono<Map<String, Object>> getSignature(@RequestBody
                                                  @Valid
                                                  @NotEmpty(message = "Request body must not be empty")
                                                  Map<String, Object> body) {
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> cloudStorageService.authenticateUpload(username, body));
    }
}

