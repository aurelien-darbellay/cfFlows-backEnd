package s05t02.interactiveCV.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.repository.utils.EntryUpdateCreator;

@RequiredArgsConstructor
public class EntryRepositoryImpl implements EntryRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private final Logger log = LoggerFactory.getLogger(EntryRepositoryImpl.class);

    @Override
    public Mono<User> insertEntryIntoDocument(String username, String docId, EntryUpdateCreator updateCreator) {
        Entry entry = updateCreator.getEntry();
        log.atDebug().log(entry.getKeyNameInDB());
        Query query = Query.query(Criteria.where("userName").is(username).and("interactiveDocuments._id").is(docId));
        Update update = new Update().push("interactiveDocuments.$.experience", entry);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        return reactiveMongoTemplate.findAndModify(query, update, options, User.class);
                /*.map(User::getInteractiveDocuments)
                .flatMapMany(Flux::fromIterable)
                .filter(doc -> (doc.getId().equals(docId)))
                .next()
                .map(InteractiveDocument::collectAllEntries)
                .flatMapMany(Flux::fromIterable)
                .filter(retrievedEntry -> retrievedEntry.getKeyNameInDB().equals(entry.getKeyNameInDB()))
                .next();*/

    }
}
