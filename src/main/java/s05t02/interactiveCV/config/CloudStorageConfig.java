package s05t02.interactiveCV.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import s05t02.interactiveCV.service.cloud.CloudStorageService;
import s05t02.interactiveCV.service.cloud.CloudinaryService;

@Configuration
public class CloudStorageConfig {

    @Value("${cloudinary.cloud-name}")
    String cloudName;
    @Value("${cloudinary.api-key}")
    String apiKey;
    @Value("${cloudinary.api-secret}")
    String apiSecret;

    @Bean
    public CloudStorageService cloudStorageService() {
        return new CloudinaryService(cloudName, apiKey, apiSecret);
    }
}
