package s05t02.interactiveCV.service.cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class CloudinaryService implements CloudStorageService {

    private final Cloudinary cloudinary;
    private static Logger log = LoggerFactory.getLogger(CloudinaryService.class);

    public CloudinaryService(String cloudName, String apiKey, String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }

    @Override
    public Mono<Map<String, Object>> authenticateUpload(String username, String fileName) {
        long timestamp = Instant.now().getEpochSecond();
        String usernameToSign = username.trim().toLowerCase();
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", timestamp);
        paramsToSign.put("folder", usernameToSign);
        paramsToSign.put("public_id", fileName);
        log.atDebug().log("Params to sign:{}", paramsToSign);
        String signature = cloudinary.apiSignRequest(paramsToSign, cloudinary.config.apiSecret);
        Map<String, Object> resp = new HashMap<>();
        resp.put("signature", signature);
        resp.put("timestamp", timestamp);
        resp.put("publicId", fileName);
        resp.put("apiKey", cloudinary.config.apiKey);
        resp.put("cloudName", cloudinary.config.cloudName);
        resp.put("folder", usernameToSign);
        return Mono.just(resp);
    }

    @Override
    public Mono<Void> saveMetaData(String username, String docId, CloudMetaData metaData, PointsToFileInCloud entry) {
        return null;
    }
}
