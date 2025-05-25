package s05t02.interactiveCV.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import s05t02.interactiveCV.model.publicViews.PublicView;

public interface PublicViewRepository extends ReactiveMongoRepository<PublicView, String> {
}
