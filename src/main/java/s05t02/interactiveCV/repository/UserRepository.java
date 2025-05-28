package s05t02.interactiveCV.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.repository.customRepos.EntryRepository;
import s05t02.interactiveCV.repository.customRepos.InteractiveDocumentRepository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>, InteractiveDocumentRepository, EntryRepository {
    Mono<User> findByUsername(String username);

    Mono<Void> deleteByUsername(String username);
}
