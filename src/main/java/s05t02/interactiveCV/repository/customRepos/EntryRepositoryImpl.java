package s05t02.interactiveCV.repository.customRepos;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.NamedEntry;

import java.security.UnresolvedPermission;

@RequiredArgsConstructor
public class EntryRepositoryImpl implements EntryRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final Logger log = LoggerFactory.getLogger(EntryRepositoryImpl.class);

    @Override
    public Mono<Entry> insertEntryIntoDocument(String username, String docId, Entry entry) {
        log.atDebug().log(entry.getKeyNameInDB());
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").is(docId));
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        return reactiveMongoTemplate.findAndModify(query, entry.createAddUpdate(), options, User.class)
                .map(User::getInteractiveDocuments)
                .flatMapMany(Flux::fromIterable)
                .filter(doc -> (doc.getId().equals(docId)))
                .next()
                .map(InteractiveDocument::collectAllEntries)
                .flatMapMany(Flux::fromIterable)
                .filter(retrievedEntry -> retrievedEntry.name().equals(entry.getKeyNameInDB()))
                .map(NamedEntry::entry)
                .next();

    }

    @Override
    public Mono<Void> deleteEntryFromDocument(String username, String docId, Entry entry) {
        log.atDebug().log(entry.getKeyNameInDB());
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").is(docId));
        Update update = new Update().unset("interactiveDocuments.summary");
        return reactiveMongoTemplate.updateFirst(query,entry.createRemoveUpdate(), User.class)
                .then();
    }

    @Override
    public Mono<Entry> updateEntryInDocument(String username, String docId,Entry updatedEntry){
        return deleteEntryFromDocument(username,docId,updatedEntry)
                .then(insertEntryIntoDocument(username,docId,updatedEntry));
    }
}
