package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.exception.MatchingFailureException;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.InteractiveDocumentType;
import s05t02.interactiveCV.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class InteractiveDocumentService {

    private final UserRepository repository;
    static private final Logger log = LoggerFactory.getLogger(InteractiveDocumentService.class);

    public Mono<InteractiveDocument> createDocumentInUser(String username,InteractiveDocumentType type, String title){
        return type.createDoc(title)
                .flatMap(doc->repository.addDocToUser(username,doc));
    }

    public Mono<InteractiveDocument> addDocumentToUser(String username, InteractiveDocument document) {
        log.atDebug().log("Adding Document :" + document.toString());
        return repository.addDocToUser(username, document)
                .switchIfEmpty(Mono.error(new MatchingFailureException(username)));
    }

    public Mono<Void> deleteDocumentFromUser(String username, String docId) {
        return repository.deleteDocInUser(username, docId);
    }

    public Mono<InteractiveDocument> updateDocumentInUser(String username, InteractiveDocument updatedDocument) {
        return repository.updateDocInUser(username, updatedDocument)
                .switchIfEmpty(Mono.error(new MatchingFailureException(username,updatedDocument.getId())));
    }

    public Mono<InteractiveDocument> getDocumentByIdInUser(String username, String docId) {
        return repository.getDocInUserById(username, docId)
                .switchIfEmpty(Mono.error(new MatchingFailureException(username,docId)));
    }
}

