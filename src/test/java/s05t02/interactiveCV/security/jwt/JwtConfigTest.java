package s05t02.interactiveCV.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(JwtConfig.class)
@SpringBootTest(classes = JwtConfig.class)
class JwtConfigTest {

    @Autowired
    JwtDecoder jwtDecoder;
    @Autowired
    JwtEncoder jwtEncoder;

    @Test
    void jwtDecoderFailsWithRandomString() {
        String test = "4444444444444449.22222.11100";
        assertThrows(Exception.class, () -> jwtDecoder.decode(test));
    }

    @Test
    void jwtDecoderValidatesWhenDecodingEncodedJwt(@Value("${spring.security.user.name}") String username) {

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("your-app")
                .subject(username)
                .claim("roles", List.of("USER"))
                .build();

        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(headers, claims));
        Jwt returnJwt = jwtDecoder.decode(jwt.getTokenValue());
        assertNotNull(returnJwt);
        assertEquals("your-app", returnJwt.getClaims().get("iss"));
    }
}