package s05t02.interactiveCV.service.security.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.ServerWebExchange;
import s05t02.interactiveCV.globalVariables.ApiPaths;

public class Auth {
    static boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
                .contains("ROLE_ADMIN");
    }

    static boolean isRightUser(Authentication authentication, ServerWebExchange exchange) {
        String pathUsername = ApiPaths.extractUserNameFromBaseUserSpaceUrl(exchange.getRequest().getURI().getPath());
        return authentication.getName().equals(pathUsername);
    }
}
