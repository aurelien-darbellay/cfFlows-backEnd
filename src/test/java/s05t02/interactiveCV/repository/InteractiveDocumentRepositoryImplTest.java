package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

@DataMongoTest
@ActiveProfiles("test")
class InteractiveDocumentRepositoryImplTest {

    static final String USERNAME = "InteractiveDocumentRepositoryImplTest-username";
    static final String USER_ID = "InteractiveDocumentRepositoryImplTest-id";
    static final String DOC_ID = "InteractiveDocumentRepositoryImplTest-docId";

    @Autowired
    private UserRepository repository;

    @Test
    void create_read_delete_read() {
        repository.save(User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .build()).block();
        repository.addDocToUser(USERNAME,
                InteractiveCv.builder().id(DOC_ID).title("My CV")
                        .build()).block();
        Mono<InteractiveDocument> result1 = repository.getDocInUserById(USERNAME, DOC_ID);
        StepVerifier.create(result1)
                .expectNextMatches(doc -> doc.getTitle().equals("My CV"))
                .verifyComplete();
        /*repository.updateDocInUser(USERNAME, InteractiveCv.builder()
                        .id(DOC_ID)
                        .title("New Title").build())
                .block();
        Mono<InteractiveDocument> result2 = repository.getDocInUserById(USERNAME, DOC_ID);
        StepVerifier.create(result2)
                .expectNextMatches(doc -> doc.getTitle().equals("New Title"))
                .verifyComplete();
        repository.deleteDocInUser(USERNAME, DOC_ID).block();
        Mono<InteractiveDocument> result3 = repository.getDocInUserById(USERNAME, DOC_ID);
        StepVerifier.create(result3).verifyComplete();
        repository.deleteById(USER_ID).block();*/
    }


}