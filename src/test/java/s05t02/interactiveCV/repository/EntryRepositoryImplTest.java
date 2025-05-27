package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Summary;

@DataMongoTest
@ActiveProfiles("test")
class EntryRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void insertEntryIntoDocument() {
        userRepository.insertEntryIntoDocument("testCV", "97584c01-47d2-4980-ab71-17875258ca0c", Summary.builder().build());
    }
}