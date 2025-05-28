package s05t02.interactiveCV.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.repository.PublicViewRepository;
import s05t02.interactiveCV.service.entities.InteractiveDocumentService;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.service.entities.UserService;

import java.time.LocalDate;
import java.time.Period;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicViewServiceTest {

    @Mock
    private PublicViewRepository publicViewRepository;

    @Mock
    private UserService userService;

    @Mock
    private InteractiveDocumentService interactiveDocumentService;

    @InjectMocks
    private PublicViewService publicViewService;

    private final String USERNAME = "johndoe";
    private final String DOC_ID = "doc123";

    private InteractiveDocument document;
    private PublicView publicView;

    @BeforeEach
    void setup() {
        document = mock(InteractiveDocument.class);
        publicView = PublicView.builder()
                .id("view1")
                .username(USERNAME)
                .document(document)
                .dateCreation(LocalDate.now())
                .dateExpiration(LocalDate.now().plus(Period.ofDays(100)))
                .build();
    }

    @Test
    void savePublicView_shouldSaveNewView() {
        when(interactiveDocumentService.updateDocumentInUser(USERNAME, document))
                .thenReturn(Mono.just(InteractiveCv.builder().build())); // user returned but unused

        when(publicViewRepository.save(any())).thenReturn(Mono.just(publicView));

        Mono<PublicView> result = publicViewService.savePublicView(USERNAME, document);

        StepVerifier.create(result)
                .expectNextMatches(saved -> saved.getUsername().equals(USERNAME))
                .verifyComplete();
    }

    @Test
    void getPublicViewById_shouldReturnAndUpdateExpiration() {
        when(publicViewRepository.findById("view1")).thenReturn(Mono.just(publicView));
        when(publicViewRepository.save(any())).thenReturn(Mono.just(publicView));

        StepVerifier.create(publicViewService.getPublicViewById("view1"))
                .expectNextMatches(view -> (view.getUsername().equals(USERNAME) && view.getDateExpiration().equals(LocalDate.now().plus(Period.ofDays(200))))
                )
                .verifyComplete();
    }

    @Test
    void getPublicViewById_shouldThrowEntityNotFound() {
        when(publicViewRepository.findById("missing")).thenReturn(Mono.empty());

        StepVerifier.create(publicViewService.getPublicViewById("missing"))
                .expectError(EntityNotFoundException.class)
                .verify();
    }

    @Test
    void deletePublicView_shouldCallRepository() {
        when(publicViewRepository.deleteById("view1")).thenReturn(Mono.empty());

        StepVerifier.create(publicViewService.deletePublicView("view1"))
                .verifyComplete();

        verify(publicViewRepository).deleteById("view1");
    }

    @Test
    void getAllPublicViews_shouldSortAndReturnFlux() {
        PublicView view2 = PublicView.builder()
                .id("view2")
                .username("alex")
                .dateCreation(LocalDate.now().minusDays(1))
                .document(document)
                .build();
        PublicView view3 = PublicView.builder()
                .id("view3")
                .username("alex")
                .dateCreation(LocalDate.now())
                .document(document)
                .build();

        when(publicViewRepository.findAll())
                .thenReturn(Flux.just(publicView, view2, view3));

        StepVerifier.create(publicViewService.getAllPublicViews())
                .expectNext(view2)
                .expectNext(view3)// alphabetically before johndoe
                .expectNext(publicView)
                .verifyComplete();
    }

    @Test
    void getAllPublicViewsFromUser_shouldFilterByUsername() {
        PublicView otherUserView = PublicView.builder()
                .id("view2")
                .username("someoneElse")
                .document(document)
                .dateCreation(LocalDate.now())
                .build();
        PublicView view3 = PublicView.builder()
                .id("view3")
                .username(USERNAME)
                .dateCreation(LocalDate.now().minus(Period.ofDays(1)))
                .document(document)
                .build();

        when(publicViewRepository.findAll())
                .thenReturn(Flux.just(publicView, otherUserView, view3));

        StepVerifier.create(publicViewService.getAllPublicViewsFromUser(USERNAME))
                .expectNext(view3)
                .expectNext(publicView)
                .verifyComplete();
    }
}
