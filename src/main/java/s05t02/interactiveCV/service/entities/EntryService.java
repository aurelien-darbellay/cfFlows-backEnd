package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.exception.MatchingFailureException;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final UserRepository repository;

    public Mono<Entry> addEntry(String username, String docId, Entry entry) {
        return repository.insertEntryIntoDocument(username, docId, entry)
                .switchIfEmpty(Mono.error(new MatchingFailureException(username,docId)));
    }

    public Mono<Void> removeEntry(String username, String docId, Entry entry) {
        return repository.deleteEntryFromDocument(username, docId, entry);
    }

    public Mono<Entry> modifyEntry(String username, String docId, Entry entry) {
        return repository.updateEntryInDocument(username, docId, entry)
                .switchIfEmpty(Mono.error(new MatchingFailureException(username, docId)));
    }
}

