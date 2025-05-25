package s05t02.interactiveCV.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUserName(username)
                .map(user -> User.builder()
                        .username(user.getUserName())
                        .password(user.getHashedPassword())
                        .roles(user.getRole().getAuthorityName().substring(5))
                        .build());
    }
}
