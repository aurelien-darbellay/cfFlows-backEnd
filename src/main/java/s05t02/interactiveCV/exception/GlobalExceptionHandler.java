package s05t02.interactiveCV.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    public record ErrorBody(String errorSimpleName, String errorMessage){};

    @ExceptionHandler({EntityNotFoundException.class, MatchingFailureException.class})
    Mono<ResponseEntity<ErrorBody>> handleEntityNotFound(Exception e){
        ErrorBody body = new ErrorBody(e.getClass().getSimpleName(),e.getMessage());
        return Mono.just(ResponseEntity.status(404).body(body));
    }
    @ExceptionHandler({JwtAuthenticationException.class, JwtException.class})
    Mono<ResponseEntity<ErrorBody>> handleJwtException(Exception e){
        ErrorBody body = new ErrorBody(e.getClass().getSimpleName(),e.getMessage());
        return Mono.just(ResponseEntity.status(401).body(body));
    }
}
