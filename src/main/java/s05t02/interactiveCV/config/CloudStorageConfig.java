package s05t02.interactiveCV.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import s05t02.interactiveCV.service.cloud.CloudStorageService;
import s05t02.interactiveCV.service.cloud.CloudinaryService;
import s05t02.interactiveCV.service.entities.UserService;

@Configuration
public class CloudStorageConfig {

    @Value("${cloudinary.cloud-name}")
    String cloudName;
    @Value("${cloudinary.api-key}")
    String apiKey;
    @Value("${cloudinary.api-secret}")
    String apiSecret;
    @Autowired
    UserService userService;

    @Bean
    public CloudStorageService cloudStorageService() {
        return new CloudinaryService(cloudName, apiKey, apiSecret, userService);
    }
}
