package s05t02.interactiveCV.controller;

import com.cloudinary.Cloudinary;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(ApiPaths.CLOUDINARY_PATH)
public class CloudinaryController {

    private final Cloudinary cloudinary;

    public CloudinaryController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PostMapping("/signature")
    public Mono<Map<String, Object>> getSignature(@PathVariable String username, @RequestBody Map<String,Object> body) {
        long timestamp = Instant.now().getEpochSecond();
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", username);
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
        Map<String, Object> resp = new HashMap<>();
        resp.put("signature", signature);
        resp.put("timestamp", timestamp);
        resp.put("apiKey",    cloudinary.config.apiKey);
        resp.put("cloudName", cloudinary.config.cloudName);
        resp.put("folder", username);
        return Mono.just(resp);
    }
}

