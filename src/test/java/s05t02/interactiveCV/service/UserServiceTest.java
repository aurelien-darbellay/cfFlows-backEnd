package s05t02.interactiveCV.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.repository.UserRepository;
import s05t02.interactiveCV.service.entities.UserService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder().build();
        mockUser.setId("123");
        mockUser.setUsername("testuser");
    }

    @Test
    void saveUser_shouldSaveSuccessfully() {
        when(userRepository.save(mockUser)).thenReturn(Mono.just(mockUser));

        StepVerifier.create(userService.saveUser(mockUser))
                .expectNext(mockUser)
                .verifyComplete();
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById("123")).thenReturn(Mono.just(mockUser));

        StepVerifier.create(userService.getUserById("123"))
                .expectNext(mockUser)
                .verifyComplete();
    }

    @Test
    void getUserById_shouldReturnErrorIfNotFound() {
        when(userRepository.findById("123")).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserById("123"))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void getUserByUserName_shouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Mono.just(mockUser));

        StepVerifier.create(userService.getUserByUserName("testuser"))
                .expectNext(mockUser)
                .verifyComplete();
    }

    @Test
    void getUserByUserName_shouldReturnErrorIfNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Mono.empty());

        StepVerifier.create(userService.getUserByUserName("testuser"))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void deleteUserById_shouldDeleteSuccessfully() {
        when(userRepository.deleteById("123")).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUserById("123"))
                .verifyComplete();
    }

    @Test
    void deleteUserByUserName_shouldDeleteSuccessfully() {
        when(userRepository.deleteByUsername("testuser")).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUserByUserName("testuser"))
                .verifyComplete();
    }
}
