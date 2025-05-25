package s05t02.interactiveCV.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.publicViews.PublicView;
import s05t02.interactiveCV.testClasses.DocumentFactory;

@DataMongoTest
@ActiveProfiles("test")
class PublicViewRepositoryTest {
    
    @Autowired
    private PublicViewRepository publicViewRepository;

    @Test
    public void whenSavingPublicView_itWorks() {
        InteractiveDocument document = DocumentFactory.populatedInteractiveCv("test-1");
        PublicView newView = PublicView.builder().username("aure").document(document).build();
        publicViewRepository.save(newView).block();
    }
}