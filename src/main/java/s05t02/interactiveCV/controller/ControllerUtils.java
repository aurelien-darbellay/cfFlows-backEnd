package s05t02.interactiveCV.controller;

import reactor.core.publisher.Mono;
import s05t02.interactiveCV.exception.JwtAuthenticationException;

import java.util.function.Function;

public class ControllerUtils {
    public static <R> Mono<R> resolveUserOrAdminOverride(
            String targetUser,
            Function<String, Mono<R>> userAction
    ) {
        if (targetUser != null && !targetUser.trim().isEmpty()) {
            return RetrieveUserInRequest.isAdmin()
                    .filter(Boolean::booleanValue)
                    .switchIfEmpty(Mono.error(new JwtAuthenticationException("User not authorized to access this space")))
                    .flatMap(isAdmin -> userAction.apply(targetUser.trim()));
        }

        return RetrieveUserInRequest.getCurrentUsername()
                .flatMap(userAction);
    }

}
