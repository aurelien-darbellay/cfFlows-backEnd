package s05t02.interactiveCV.service.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.globalVariables.ApiPaths;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtCookieSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        createJwtCookie(exchange,authentication);
        // You can redirect or just complete response here
        exchange.getResponse().setStatusCode(HttpStatus.FOUND);
        exchange.getResponse().getHeaders().add(HttpHeaders.LOCATION, ApiPaths.USER_DASHBOARD_PATH.replace("{username}", authentication.getName()));
        return exchange.getResponse().setComplete();
    }

    public ServerWebExchange createJwtCookie(ServerWebExchange exchange, Authentication authentication){
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(granted -> String.valueOf(granted.getAuthority()))
                .toList();

        var jwt = jwtUtils.createJwt(username, roles);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt.getTokenValue())
                .httpOnly(true)
                .secure(false) // Set to true if using HTTPS
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofHours(2))
                .build();
        exchange.getResponse().addCookie(cookie);
        return exchange;
    }
}

