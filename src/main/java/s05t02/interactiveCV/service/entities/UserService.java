package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import s05t02.interactiveCV.dto.DashBoardDto;
import s05t02.interactiveCV.dto.UserUpdateRequestDto;
import s05t02.interactiveCV.dto.interfaces.UserMapableToDto;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.repository.UserRepository;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public Mono<User> saveUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} and userName : {} saved.", retrievedUser.getId(), retrievedUser.getUsername()))
                .doOnError(error -> log.error("Error saving user with id : {} - Error Message: {}", user.getId(), error.getMessage()));
    }

    public Mono<DashBoardDto> updateUser(UserUpdateRequestDto dto) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .flatMap(userRepository::findByUsername)
                .flatMap(user -> {
                    updateNonNullField(user, dto);
                    return userRepository.save(user);
                })
                .map(UserMapableToDto::mapToDto);
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(id)))
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} and userName : {} retrieved.", retrievedUser.getId(), retrievedUser.getUsername()))
                .doOnError(error -> log.error("Error retrieving user with id : {} - Error Message: {}", id, error.getMessage()));
    }

    public Mono<User> getUserByUserName(String userName) {
        return userRepository.findByUsername(userName)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(userName)))
                .doOnSuccess(retrievedUser -> log.debug("User with username : {} and userName : {} retrieved.", retrievedUser.getUsername(), retrievedUser.getUsername()))
                .doOnError(error -> log.error("Error retrieving user with username : {} - Error Message: {}", userName, error.getMessage()));
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteById(id)
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} deleted.", id))
                .doOnError(error -> log.error("Error deleting user with id : {}", id));
    }

    public Mono<Void> deleteUserByUserName(String username) {
        return userRepository.deleteByUsername(username)
                .doOnSuccess(retrievedUser -> log.debug("User with username : {} deleted.", username))
                .doOnError(error -> log.error("Error deleting user with username : {}", username));
    }

    public Flux<User> getAllUser() {
        return userRepository.findAll()
                .sort(Comparator.comparing(User::getUsername))
                .index()
                .doOnNext(tuple -> log.debug("User number {}, with username {}, retrieved successfully", tuple.getT1(), tuple.getT2().getUsername()))
                .doOnError(error -> log.error("Error retrieving user, message : {}", error.getMessage()))
                .map(Tuple2::getT2);
    }

    private void updateNonNullField(User user, UserUpdateRequestDto dto) {
        Optional.ofNullable(dto.getUsername())
                .ifPresent(user::setUsername);
        Optional.ofNullable(dto.getLastname())
                .ifPresent(user::setLastname);
        Optional.ofNullable(dto.getPassword())
                .ifPresent(pw -> user.setHashedPassword(encoder.encode(pw)));
        Optional.ofNullable(dto.getFirstname())
                .ifPresent(user::setFirstname);
    }
}
