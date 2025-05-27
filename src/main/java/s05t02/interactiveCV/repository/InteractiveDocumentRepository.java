package s05t02.interactiveCV.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

@Repository
public interface InteractiveDocumentRepository {
    Mono<InteractiveDocument> addDocToUser(String username, InteractiveDocument document);

    Mono<Void> deleteDocInUser(String username, String docId);

    Mono<InteractiveDocument> updateDocInUser(String username, InteractiveDocument updatedDoc);
}
