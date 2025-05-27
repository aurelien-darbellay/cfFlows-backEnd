package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Experience;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.repository.utils.ListEntryUpdateCreator;

@DataMongoTest
@ActiveProfiles("test")
class EntryRepositoryImplTest {

    static final String USERNAME = "EntryRepositoryImplTest-username";
    static final String USER_ID = "EntryRepositoryImplTest-id";
    static final String DOC_ID = "EntryRepositoryImplTest-docId";

    @Autowired
    private UserRepository repository;


    @Test
    void testInsertEntryIntoDocument() {
        repository.save(User.builder()
                .id(USER_ID)
                .userName(USERNAME)
                .build()).block();
        repository.addDocToUser(USERNAME,
                InteractiveCv.builder().id(DOC_ID).title("My CV")
                        .build()).block();
        /*Entry newEntry = Summary.builder().title("Yo").build();
        repository.insertEntryIntoDocument(USERNAME, DOC_ID, new SimpleEntryUpdateCreator(newEntry)).block();*/
        Entry anotherEntry = Experience.builder().description("buufff").build();
        repository.insertEntryIntoDocument(USERNAME, DOC_ID, new ListEntryUpdateCreator(anotherEntry)).block();

    }
}