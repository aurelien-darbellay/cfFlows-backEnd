package s05t02.interactiveCV.service.security.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

public class AdminSpaceAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final static Logger log = LoggerFactory.getLogger(AdminSpaceAuthorizationManager.class);


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext context) {
        log.atDebug().log("In admin authorization manager");
        return authenticationMono
                .map(authentication ->
                {
                    log.atDebug().log("Admin authentication found : " + Auth.isAdmin(authentication));
                    return new AuthorizationDecision(Auth.isAdmin(authentication));
                })
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
