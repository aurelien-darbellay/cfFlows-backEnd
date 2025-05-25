package s05t02.interactiveCV.model.documents.cv;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s05t02.interactiveCV.testClasses.DocumentFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InteractiveCvTest {

    private final static Logger log = LoggerFactory.getLogger(InteractiveCvTest.class);

    @Test
    void getProjectedDocument() {

        InteractiveCv original = DocumentFactory.populatedInteractiveCv("cv-123");

        InteractiveCv projectedCV = original.getProjectedDocument();
        assertEquals(0, projectedCV.getLanguages().size());
        assertNull(projectedCV.getPicture());
        assertNull(projectedCV.getSoftSkills());
        assertEquals(2, projectedCV.getTechnicalSkills().size());
        assertEquals(1, projectedCV.getEducation().size());
        log.info(projectedCV.getEducation().get(0).toString());
        log.info(projectedCV.getEducation().toString());
        assertEquals("Harvard", projectedCV.getEducation().get(0).getTrainingCenter());
    }
}