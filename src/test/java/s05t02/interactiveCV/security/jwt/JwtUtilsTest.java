package s05t02.interactiveCV.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import s05t02.interactiveCV.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;

    @Test
    void createJwtTest() {
        Jwt jwt = jwtUtils.createJwt("alice", List.of(Role.ADMIN.getAuthorityName()));
        assertEquals("alice", jwt.getClaims().get("sub"));
        assertInstanceOf(List.class, jwt.getClaims().get("roles"));
        assertSame(Role.ADMIN.getAuthorityName(), ((List<String>) jwt.getClaims().get("roles")).get(0));
    }
}