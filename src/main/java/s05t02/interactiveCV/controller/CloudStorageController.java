package s05t02.interactiveCV.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.service.cloud.CloudStorageService;

import java.util.Map;

@RestController
@RequestMapping(ApiPaths.CLOUD_STORAGE_PATH)
public class CloudStorageController {

    private final CloudStorageService cloudStorageService;

    public CloudStorageController(CloudStorageService cloudStorageService) {
        this.cloudStorageService = cloudStorageService;
    }

    @PostMapping("/signature")
    public Mono<Map<String, Object>> getSignature(@PathVariable String username, @RequestBody Map<String, Object> body) {
        return cloudStorageService.authenticateUpload(username, body);
    }
}

