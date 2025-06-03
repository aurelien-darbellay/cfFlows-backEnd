package s05t02.interactiveCV.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;
import s05t02.interactiveCV.service.entities.UserService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryService implements CloudStorageService {

    private final Cloudinary cloudinary;
    private final UserService userService;

    public CloudinaryService(String cloudName, String apiKey, String apiSecret, UserService userService) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
        this.userService = userService;
    }

    @Override
    public Mono<Map<String, Object>> authenticateUpload(String username, Map<String, Object> body) {
        long timestamp = Instant.now().getEpochSecond();
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", username);
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
        Map<String, Object> resp = new HashMap<>();
        resp.put("signature", signature);
        resp.put("timestamp", timestamp);
        resp.put("apiKey", cloudinary.config.apiKey);
        resp.put("cloudName", cloudinary.config.cloudName);
        resp.put("folder", username);
        return Mono.just(resp);
    }

    @Override
    public Mono<Void> saveMetaData(String username, String docId, CloudMetaData metaData, PointsToFileInCloud entry) {
        return null;
    }
}
