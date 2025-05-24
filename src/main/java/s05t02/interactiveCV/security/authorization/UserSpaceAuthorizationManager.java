package s05t02.interactiveCV.security.authorization;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;

public class UserSpaceAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final static Logger log = LoggerFactory.getLogger(UserSpaceAuthorizationManager.class);

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext context) {
        log.debug("In authorization manager");
        ServerWebExchange exchange = context.getExchange();
        String pathUsername = ApiPaths.extractUserNameFromBaseUserSpaceUrl(exchange.getRequest().getURI().getPath());
        return authenticationMono
                .map(authentication -> new AuthorizationDecision(authentication.getName().equals(pathUsername)))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
}
