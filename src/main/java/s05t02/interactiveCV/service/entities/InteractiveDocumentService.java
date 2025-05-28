package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.repository.customRepos.InteractiveDocumentRepository;

@Service
@RequiredArgsConstructor
public class InteractiveDocumentService {

    private final InteractiveDocumentRepository interactiveDocumentRepository;

    public Mono<InteractiveDocument> addDocumentToUser(String username, InteractiveDocument document) {
        return interactiveDocumentRepository.addDocToUser(username, document);
    }

    public Mono<Void> deleteDocumentFromUser(String username, String docId) {
        return interactiveDocumentRepository.deleteDocInUser(username, docId);
    }

    public Mono<InteractiveDocument> updateDocumentInUser(String username, InteractiveDocument updatedDocument) {
        return interactiveDocumentRepository.updateDocInUser(username, updatedDocument);
    }

    public Mono<InteractiveDocument> getDocumentByIdForUser(String username, String docId) {
        return interactiveDocumentRepository.getDocInUserById(username, docId);
    }
}

