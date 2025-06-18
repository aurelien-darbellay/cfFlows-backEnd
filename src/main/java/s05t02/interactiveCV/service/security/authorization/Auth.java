package s05t02.interactiveCV.service.security.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;

public class Auth {
    static final private Logger log = LoggerFactory.getLogger(Auth.class);

    static boolean isAdmin(Authentication authentication) {
        log.atDebug().log(authentication.toString());
        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> {
                    log.atDebug().log("Authority : " + grantedAuthority.getAuthority());
                    return grantedAuthority.getAuthority();
                })
                .toList()
                .contains("ROLE_ADMIN");
    }

    static boolean isRightUser(Authentication authentication, ServerWebExchange exchange) {
        log.atDebug().log(authentication.toString());
        return true;
    }
}
