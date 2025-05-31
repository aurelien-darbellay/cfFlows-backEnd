package s05t02.interactiveCV.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.exception.EntityNotFoundException;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.service.entities.UserService;
import s05t02.interactiveCV.testClasses.DocumentFactory;

@SpringBootTest
public class PVServiceIntegratedTest {
    @Autowired
    PublicViewService publicViewService;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userService.saveUser(User.builder().username("pv-test").build()).block();
    }

    @Test
    void create_get_deletePublicView() {
        InteractiveCv cv = DocumentFactory.populatedInteractiveCv("lola");
        Mono<PublicView> result1 = publicViewService.savePublicView("pv-test", cv)
                .flatMap(pv -> publicViewService.getPublicViewById(pv.getId()));
        StepVerifier.create(result1)
                .expectNextMatches(pv -> ((InteractiveCv) pv.getDocument()).getEducation().get(0).getTrainingCenter().equals("Harvard"))
                .verifyComplete();
    }
    @Test

    void getPublicViewById() {
        Mono<PublicView> result1 = publicViewService.getPublicViewById("uuu");
        StepVerifier.create(result1)
                .expectError(EntityNotFoundException.class);
    }

    @AfterEach
    void cleanUp() {
        userService.deleteUserByUserName("pv-test").block();
    }
}
