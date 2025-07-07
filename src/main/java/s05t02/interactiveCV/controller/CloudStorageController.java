package s05t02.interactiveCV.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<Map<String, Object>> getSignature(@RequestBody Map<String, String> fileInfo) {
        String fileName = fileInfo.get("fileName");
        if (fileName == null || fileName.trim().isEmpty()) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required parameter: fileName"));
        }
        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(username -> cloudStorageService.authenticateUpload(username, fileName));
    }

    @PostMapping("/delete")
    public Mono<Void> deleteAsset(@RequestBody Map<String, String> fileInfo) {
        String publicId = fileInfo.get("publicId");
        return cloudStorageService.deleteAsset(publicId);
    }
}

