package s05t02.interactiveCV.service.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtCookieSuccessHandler implements ServerAuthenticationSuccessHandler {

    @Autowired
    ObjectMapper objectMapper;

    @Value("${cookie.sameSite}")
    private String jwtSameSite;

    @Value("${cookie.secure}")
    private boolean jwtSecure;

    private final JwtUtils jwtUtils;
    private static final Logger log = LoggerFactory.getLogger(JwtCookieSuccessHandler.class);

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.atDebug().log("Authentication received" + authentication.toString());
        ServerWebExchange exchange = webFilterExchange.getExchange();
        Jwt jwt = createJwtCookie(exchange, authentication);
        log.atDebug().log("Jwt created :" + jwt.getClaims().toString());
        Map<String, Object> bodyMap = Map.of(
                "username", ((UserDetails) authentication.getPrincipal()).getUsername(),
                "authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
        String bodyJson;
        try {
            bodyJson = objectMapper.writeValueAsString(bodyMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ServerHttpResponse response = exchange.getResponse();
        DataBuffer buffer = response.bufferFactory().wrap(bodyJson.getBytes(StandardCharsets.UTF_8));
        response.setStatusCode(HttpStatus.OK);
        return response.writeWith(Mono.just(buffer));
    }

    public Jwt createJwtCookie(ServerWebExchange exchange, Authentication authentication) {
        String username = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(granted -> String.valueOf(granted.getAuthority()))
                .toList();

        Jwt jwt = jwtUtils.createJwt(username, roles);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt.getTokenValue())
                .httpOnly(true)
                .secure(jwtSecure) // Set to true if using HTTPS
                .path("/")
                .sameSite(jwtSameSite)
                .maxAge(Duration.ofHours(2))
                .build();
        exchange.getResponse().addCookie(cookie);
        return jwt;
    }
}

