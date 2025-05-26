package s05t02.interactiveCV.service.entities;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.repository.PublicViewRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class PublicViewService {
    private final PublicViewRepository publicViewRepository;
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(PublicViewService.class);

    public Mono<PublicView> savePublicView(String username, InteractiveDocument document) {
        PublicView newView = createNewPublicView(username, document);
        return userService.updateDocumentFromUserByUserName(username, document)
                .flatMap(user -> publicViewRepository.save(newView))
                .doOnSuccess(savedView -> log.debug("Document with id : {}, of type : {} saved as public view by {}.", document.getId(), document.getClass(), username))
                .doOnError(error -> log.error("Error saving document with id : {} for user {} - Error Message: {}", document.getId(), username, error.getMessage()));
    }

    public Mono<PublicView> getPublicViewById(String id) {
        return publicViewRepository.findById(id)
                .flatMap(this::updatePublicViewExpirationDate)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(id)))
                .doOnSuccess(retrievedDoc -> log.debug("Document with id : {} and type : {} retrieved.", retrievedDoc.getId(), retrievedDoc.getClass()))
                .doOnError(error -> log.error("Error retrieving document with id : {} - Error Message: {}", id, error.getMessage()));
    }

    public Mono<Void> deletePublicView(String id) {
        return publicViewRepository.deleteById(id)
                .doOnSuccess(retrievedUser -> log.debug("Public view with id : {} deleted.", id))
                .doOnError(error -> log.error("Error deleting public view with id : {}", id));
    }

    public Flux<PublicView> getAllPublicViews() {
        return publicViewRepository.findAll()
                .sort(Comparator.comparing(PublicView::getUsername).thenComparing(PublicView::getDateCreation))
                .index()
                .doOnNext(tuple -> log.debug("Public view number {}, from user {}, retrieved successfully", tuple.getT1(), tuple.getT2().getUsername()))
                .doOnError(error -> log.error("Error retrieving public view, message : {}", error.getMessage()))
                .map(Tuple2::getT2);
    }

    public Flux<PublicView> getAllPublicViewsFromUser(String username) {
        return publicViewRepository.findAll()
                .filter(publicView -> publicView.getUsername().equals(username))
                .sort(Comparator.comparing(PublicView::getDateCreation))
                .index()
                .doOnNext(tuple -> log.debug("Retrieved public view number {}, from user {}", tuple.getT1(), username))
                .doOnError(error -> log.error("Error retrieving public view for user {}, message : {}", username, error.getMessage()))
                .map(Tuple2::getT2);
    }

    private PublicView createNewPublicView(String username, InteractiveDocument document) {
        return PublicView.builder()
                .username(username)
                .document(document.getProjectedDocument())
                .dateCreation(LocalDate.now())
                .dateExpiration(LocalDate.now().plus(Period.ofDays(200)))
                .build();
    }

    private Mono<PublicView> updatePublicViewExpirationDate(PublicView view) {
        view.setDateExpiration(LocalDate.now().plus(Period.ofDays(200)));
        return publicViewRepository.save(view);
    }

}
