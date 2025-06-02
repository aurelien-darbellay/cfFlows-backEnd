package s05t02.interactiveCV.service.security.jwt;

import jakarta.validation.UnexpectedTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import s05t02.interactiveCV.exception.JwtAuthenticationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                        log.atDebug().log("JWT validated with claims : " + jwt.getClaims());
                        Authentication auth = new JwtAuthenticationToken(jwt,getAuthorities(jwt));
                        log.atDebug().log("Auth loaded in security context:" + auth.toString());
                        return Mono.just(new SecurityContextImpl(auth));
                    } catch (JwtException | JwtAuthenticationException e) {
                        log.atDebug().log("invalid JWT");
                        return Mono.error(new JwtAuthenticationException(e.getMessage())); // invalid JWT
                    }
                });
    }

    private List<SimpleGrantedAuthority> getAuthorities(Jwt jwt) throws JwtAuthenticationException {
        Map<String, Object> claims = jwt.getClaims();
        Object rolesClaim = claims.get("roles");
        List<String> roles = new ArrayList<>();
        if (rolesClaim instanceof List<?>) {
            for (Object element : (List<?>) rolesClaim) {
                if (element instanceof String) {
                    roles.add((String) element);
                } else {
                    throw new JwtAuthenticationException("Error: roles in jwt should be stored as string: impossible to create authorities");
                }
            }
        } else {
            throw new JwtAuthenticationException("Error: roles in jwt should be stored as a list of strings: impossible to create authorities");
        }
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }
}
