package s05t02.interactiveCV.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.repository.utils.EntryUpdateCreator;

@Repository
public interface EntryRepository {
    Mono<User> insertEntryIntoDocument(String username, String docId, EntryUpdateCreator updateCreator);
}
