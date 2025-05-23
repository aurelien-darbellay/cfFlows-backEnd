package s05t02.interactiveCV.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import s05t02.interactiveCV.model.Role;

import java.util.List;

@Component
public class JwtUtils {

    @Autowired
    private JwtEncoder jwtEncoder;

    public Jwt createJwt(String username, List<Role> roles) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("app-interactiveCV")
                .subject(username)
                .claim("roles", roles)
                .build();
        JwsHeader headers = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims));
    }
}
