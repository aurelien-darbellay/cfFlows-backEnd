package s05t02.interactiveCV.repository.customRepos;

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
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.util.Collections;


@RequiredArgsConstructor
public class InteractiveDocumentRepositoryImpl implements InteractiveDocumentRepository {
    private final ReactiveMongoTemplate reactiveMongoTemplate;
    private static Logger log = LoggerFactory.getLogger(InteractiveDocumentRepository.class);

    @Override
    public Mono<InteractiveDocument> addDocToUser(String username, InteractiveDocument document) {
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").ne(document.getId()));
        Update update = new Update().push("interactiveDocuments", document);
        return reactiveMongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), User.class)
                .flatMap(user ->
                        Mono.justOrEmpty(user.getInteractiveDocuments().stream()
                                .filter(doc -> {
                                    log.debug("Retrieved doc id = {}; introduced doc id = {}", doc.getId(), document.getId());
                                    return doc.getId().equals(document.getId());
                                })
                                .findFirst())
                );
    }

    @Override
    public Mono<Void> deleteDocInUser(String username, String docId) {
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").is(docId));
        Update update = new Update().pull("interactiveDocuments", Collections.singletonMap("_id", docId));
        return reactiveMongoTemplate.updateFirst(query, update, User.class)
                .then();
    }

    @Override
    public Mono<InteractiveDocument> updateDocInUser(String username, InteractiveDocument updatedDoc) {
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").is(updatedDoc.getId()));
        Update update = new Update().set("interactiveDocuments.$", updatedDoc);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        return reactiveMongoTemplate.findAndModify(query, update, options, User.class)
                .flatMap(user ->
                        Mono.justOrEmpty(user.getInteractiveDocuments().stream()
                                .filter(doc -> doc.getId().equals(updatedDoc.getId()))
                                .findFirst()));
    }

    @Override
    public Mono<InteractiveDocument> getDocInUserById(String username, String docId) {
        Query query = Query.query(Criteria.where("username").is(username).and("interactiveDocuments._id").is(docId));
        return reactiveMongoTemplate.findOne(query, User.class)
                .flatMap(user -> Mono.justOrEmpty(
                        user.getInteractiveDocuments().stream()
                                .filter(doc -> doc.getId().equals(docId))
                                .findFirst()));
    }
}
