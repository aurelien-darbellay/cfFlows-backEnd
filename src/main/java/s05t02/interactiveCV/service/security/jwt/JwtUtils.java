package s05t02.interactiveCV.service.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class JwtUtils {

    @Autowired
    private JwtEncoder jwtEncoder;

    public Jwt createJwt(String username, List<String> roles) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("app-interactiveCV")
                .subject(username)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(Duration.ofHours(1)))
                .claim("roles", roles)
                .build();
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims));
    }
}
