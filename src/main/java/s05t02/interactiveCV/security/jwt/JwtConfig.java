package s05t02.interactiveCV.security.jwt;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    @Bean
    public JwtDecoder jwtDecoder(@Value("${jwt.secret-base64}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        SecretKey key = new SecretKeySpec(bytes, MacAlgorithm.HS256.getName());
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(@Value("${jwt.secret-base64}") String secret) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        SecretKey key = new SecretKeySpec(bytes, MacAlgorithm.HS256.getName());
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }
}
