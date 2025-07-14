package s05t02.interactiveCV.service.security.jwt;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private static final String JWT_COOKIE_NAME = "jwt";
    private static final String XSRF_COOKIE_NAME = "XSRF-TOKEN";

    @Value("${cookie.secure}")
    private boolean jwtSecure;

    @Value("${cookie.sameSite}")
    private String jwtSameSite;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        ResponseCookie expiredCookie = ResponseCookie.from(JWT_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(jwtSecure)
                .sameSite(jwtSameSite)// Set to true if using HTTPS
                .path("/")
                .maxAge(0)
                .build();

        exchange.getExchange().getResponse().addCookie(expiredCookie);
        exchange.getExchange().getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);

        return exchange.getExchange().getResponse().setComplete();
    }
}


