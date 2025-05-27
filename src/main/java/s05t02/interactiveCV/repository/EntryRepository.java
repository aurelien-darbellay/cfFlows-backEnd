package s05t02.interactiveCV.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

@Repository
public interface EntryRepository {
    Mono<Entry> insertEntryIntoDocument(String username, String docId, Entry entry);
}
