package s05t02.interactiveCV.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtCookieSecurityContextRepository implements ServerSecurityContextRepository {

    private final Logger log = LoggerFactory.getLogger(JwtCookieSecurityContextRepository.class);
    private final JwtDecoder jwtDecoder;

    public JwtCookieSecurityContextRepository(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty(); // we don't store anything server-side
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("jwt"))
                .map(HttpCookie::getValue)
                .doOnNext(token -> log.atDebug().log("JWT in cookie: " + token))
                .flatMap(token -> {
                    try {
                        Jwt jwt = jwtDecoder.decode(token);
                        log.atDebug().log("JWT validated");
                        Authentication auth = new JwtAuthenticationToken(jwt);
                        auth.setAuthenticated(true);
                        return Mono.just(new SecurityContextImpl(auth));
                    } catch (JwtException e) {
                        log.atDebug().log("invalid JWT");
                        return Mono.empty(); // invalid JWT
                    }
                });
    }
}
