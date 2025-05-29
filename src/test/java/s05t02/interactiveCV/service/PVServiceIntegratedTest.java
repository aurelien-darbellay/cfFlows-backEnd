package s05t02.interactiveCV.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.service.entities.PublicViewService;
import s05t02.interactiveCV.testClasses.DocumentFactory;

@SpringBootTest
public class PVServiceIntegratedTest {
    @Autowired
    PublicViewService publicViewService;

    @Test
    void create_get_deletePublicView() {
        InteractiveCv cv = DocumentFactory.populatedInteractiveCv("lola");
        publicViewService.savePublicView("Aure", cv).block();

    }
}
