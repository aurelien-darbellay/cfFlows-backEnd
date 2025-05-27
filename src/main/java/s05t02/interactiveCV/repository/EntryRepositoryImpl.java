package s05t02.interactiveCV.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

public class EntryRepositoryImpl implements EntryRepository {
    private final Logger log = LoggerFactory.getLogger(EntryRepositoryImpl.class);

    @Override
    public Mono<Entry> insertEntryIntoDocument(String username, String docId, Entry entry) {
        log.atDebug().log(entry.getKeyNameInDB());
        
        return null;
    }
}
