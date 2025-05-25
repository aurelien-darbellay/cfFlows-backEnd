package s05t02.interactiveCV.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public Mono<User> saveUser(User user) {
        return userRepository.save(user)
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} and userName : {} saved.", retrievedUser.getId(), retrievedUser.getUserName()))
                .doOnError(error -> log.error("Error saving user with id : {} - Error Message: {}", user.getId(), error.getMessage()));
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(id)))
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} and userName : {} retrieved.", retrievedUser.getId(), retrievedUser.getUserName()))
                .doOnError(error -> log.error("Error retrieving user with id : {} - Error Message: {}", id, error.getMessage()));
    }

    public Mono<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(userName)))
                .doOnSuccess(retrievedUser -> log.debug("User with username : {} and userName : {} retrieved.", retrievedUser.getUserName(), retrievedUser.getUserName()))
                .doOnError(error -> log.error("Error retrieving user with username : {} - Error Message: {}", userName, error.getMessage()));
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteById(id)
                .doOnSuccess(retrievedUser -> log.debug("User with id : {} deleted.", id))
                .doOnError(error -> log.error("Error deleting user with id : {}", id));
    }

    public Mono<Void> deleteUserByUserName(String username) {
        return userRepository.deleteByUserName(username)
                .doOnSuccess(retrievedUser -> log.debug("User with username : {} deleted.", username))
                .doOnError(error -> log.error("Error deleting user with username : {}", username));
    }

    public Mono<User> updateDocumentFromUserByUserName(String username, InteractiveDocument document) {
        return userRepository.findByUserName(username)
                .map(user -> {
                    List<InteractiveDocument> documents = user.getDocuments();
                    documents.removeIf(interactiveDocument -> interactiveDocument.getId().equals(document.getId()));
                    documents.add(document);
                    user.setDocuments(documents);
                    return user;
                })
                .flatMap(this::saveUser);
    }
}
