package s05t02.interactiveCV.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.User;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUserName(String username);

    Mono<Void> deleteByUserName(String username);
}
