package s05t02.interactiveCV.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

public interface PublicViewsRepository extends ReactiveMongoRepository<InteractiveDocument, String> {
}
