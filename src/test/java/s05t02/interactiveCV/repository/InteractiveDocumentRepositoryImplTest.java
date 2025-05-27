package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;

@DataMongoTest
@ActiveProfiles("test")
class InteractiveDocumentRepositoryImplTest {

    @Autowired
    private UserRepository repository;

    @Test
    void whenAddingDocTuUser_docAdded() {
        repository.addDocToUser("duplicate", InteractiveCv.builder().id("my_cv").title("My CV").build()).block();
    }

    @Test
    void whenDeleting_docDeleted() {
        repository.deleteDocInUser("duplicate", "my_cv").block();
    }

    @Test
    void whenUpdating_valueChange() {
        repository.addDocToUser("duplicate", InteractiveCv.builder().id("my_cv").title("My CV").build()).block();
        repository.updateDocInUser("duplicate", InteractiveCv.builder().id("my_cv").title("Whallah").build()).block();
    }

}