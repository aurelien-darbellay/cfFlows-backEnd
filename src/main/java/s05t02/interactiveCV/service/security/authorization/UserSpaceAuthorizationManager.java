package s05t02.interactiveCV.service.security.authorization;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UserSpaceAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final static Logger log = LoggerFactory.getLogger(UserSpaceAuthorizationManager.class);

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext context) {
        log.debug("In user authorization manager");
        ServerWebExchange exchange = context.getExchange();
        return authenticationMono
                .map(authentication -> new AuthorizationDecision(Auth.isRightUser(authentication, exchange) || Auth.isAdmin(authentication)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
