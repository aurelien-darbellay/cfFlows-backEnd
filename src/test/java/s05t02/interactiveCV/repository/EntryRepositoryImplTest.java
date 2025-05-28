package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Experience;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Portfolio;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Summary;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ListEntries;

import java.util.List;


@DataMongoTest
@ActiveProfiles("test")
class EntryRepositoryImplTest {

    static final String USERNAME = "EntryRepositoryImplTest-username";
    static final String USER_ID = "EntryRepositoryImplTest-id";
    static final String DOC_ID = "EntryRepositoryImplTest-docId";
    static final Logger log = LoggerFactory.getLogger(EntryRepositoryImplTest.class);
    @Autowired
    private UserRepository repository;

    @BeforeEach
    void setUp(){
        repository.save(User.builder()
                .id(USER_ID)
                .username(USERNAME)
                .build()).block();
    }

    @Test
    void testInsertEntryIntoDocument() {
        repository.addDocToUser(USERNAME,
                InteractiveCv.builder().id(DOC_ID).title("My CV")
                        .build()).block();
        Entry newEntry = Summary.builder().title("Yo").build();
        Mono<Entry> result0 = repository.insertEntryIntoDocument(USERNAME, DOC_ID, newEntry);
        StepVerifier.create(result0)
                .expectNextMatches(entry -> entry.getKeyNameInDB().equals("summary"))
                .verifyComplete();
        Entry anotherEntry = Experience.builder().description("buufff").build();
        Mono<Entry> result = repository.insertEntryIntoDocument(USERNAME, DOC_ID, anotherEntry);
        StepVerifier.create(result)
                .expectNextMatches(entry->entry.getKeyNameInDB().equals("experience"))
                .verifyComplete();
        repository.deleteByUsername(USERNAME).block();
    }

    @Test
    void deleteEntryFromDocument(){
        Portfolio portfolioElem = Portfolio.builder().build();
        Summary summary = Summary.builder().build();
        ListEntries<Portfolio> entry = ListEntries.of(List.of(portfolioElem), Portfolio.class);
        repository.addDocToUser(USERNAME,
                InteractiveCv.builder().id(DOC_ID).summary(Summary.builder().build())
                        .title("My CV")
                        .summary(summary)
                        .portfolio(entry)
                        .build()).block();
        Mono<Void> resultVoid = repository.deleteEntryFromDocument(USERNAME,DOC_ID,summary);
        StepVerifier.create(resultVoid)
                .verifyComplete();
    }

    @Test
    void updateEntreInDoc(){
        Portfolio portfolioElem = Portfolio.builder().build();
        Summary summary = Summary.builder().build();
        ListEntries<Portfolio> entry = ListEntries.of(List.of(portfolioElem), Portfolio.class);
        repository.addDocToUser(USERNAME,
                InteractiveCv.builder().id(DOC_ID).summary(Summary.builder().build())
                        .title("My CV")
                        .summary(summary)
                        .portfolio(entry)
                        .build()).block();
        summary.setText("bla");
        Mono<Entry> result = repository.updateEntryInDocument(USERNAME,DOC_ID,summary);
        StepVerifier.create(result)
                .expectNextMatches(summ->((Summary)summ).getText().equals("bla"))
                .verifyComplete();
        portfolioElem.setProjectName("gnoki");
        Mono<Entry> result1 = repository.updateEntryInDocument(USERNAME,DOC_ID,portfolioElem);
        StepVerifier.create(result1)
                .expectNextMatches(list->((ListEntries<Portfolio>)list).get(0).getProjectName().equals("gnoki"))
                .verifyComplete();
    }
}