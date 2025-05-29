package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class InteractiveDocumentService {

    private final UserRepository repository;
    static private final Logger log = LoggerFactory.getLogger(InteractiveDocumentService.class);

    public Mono<InteractiveDocument> addDocumentToUser(String username, InteractiveDocument document) {
        log.atDebug().log("Adding Document :" + document.toString());
        return repository.addDocToUser(username, document);
    }

    public Mono<Void> deleteDocumentFromUser(String username, String docId) {
        return repository.deleteDocInUser(username, docId);
    }

    public Mono<InteractiveDocument> updateDocumentInUser(String username, InteractiveDocument updatedDocument) {
        return repository.updateDocInUser(username, updatedDocument);
    }

    public Mono<InteractiveDocument> getDocumentByIdForUser(String username, String docId) {
        return repository.getDocInUserById(username, docId);
    }
}

