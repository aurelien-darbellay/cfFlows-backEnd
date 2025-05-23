package s05t02.interactiveCV.security.jwt;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class JwtCookieLoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;

    public JwtCookieLoginSuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
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

        // You can redirect or just complete response here
        return exchange.getResponse().setComplete();
    }
}

