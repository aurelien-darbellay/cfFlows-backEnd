package s05t02.interactiveCV.repository.customRepos;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;


@Repository
public interface EntryRepository {
    Mono<Entry> insertEntryIntoDocument(String username, String docId, Entry entry);
    Mono<Void> deleteEntryFromDocument(String username, String docId, Entry entry);
    Mono<Entry> updateEntryInDocument(String username, String docId, Entry updatedEntry);
}
