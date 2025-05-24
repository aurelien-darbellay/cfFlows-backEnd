package s05t02.interactiveCV.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import s05t02.interactiveCV.globalVariables.ApiPaths;
import s05t02.interactiveCV.security.authorization.AdminSpaceAuthorizationManager;
import s05t02.interactiveCV.security.authorization.UserSpaceAuthorizationManager;
import s05t02.interactiveCV.security.jwt.JwtCookieLoginSuccessHandler;
import s05t02.interactiveCV.security.jwt.JwtCookieSecurityContextRepository;

import java.util.List;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtCookieSecurityContextRepository jwtCookieSecurityContextRepository, JwtCookieLoginSuccessHandler jwtSuccessHandler) {
        log.debug("Security filter chain initialized with JWT cookie support");
        return http
                .csrf(csrfSpec -> csrfSpec.csrfTokenRepository(csrfTokenRepository()))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .securityContextRepository(jwtCookieSecurityContextRepository)
                .formLogin(formLoginSpec -> formLoginSpec.authenticationSuccessHandler(jwtSuccessHandler))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(ApiPaths.USER_BASE_PATH.replace("{username}", "**"))
                        .access(userSpaceAuthManager())
                        .pathMatchers(ApiPaths.ADMIN_BASE_PATH)
                        .access(adminSpaceAuthManager())
                        .pathMatchers(ApiPaths.PROTECTED_BASE_PATH + "/**").authenticated()
                        .anyExchange().permitAll()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public ServerCsrfTokenRepository csrfTokenRepository() {
        CookieServerCsrfTokenRepository csrfTokenRepository = CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookieCustomizer(builder -> builder
                .sameSite("Lax")   // or "Lax", "None"
                .secure(false)         // important if using SameSite=None
                .path("/")            // optional, sets cookie path
        );
        return csrfTokenRepository;
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> userSpaceAuthManager() {
        return new UserSpaceAuthorizationManager();
    }

    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> adminSpaceAuthManager() {
        return new AdminSpaceAuthorizationManager();
    }


}
