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
    void projectDocument() {

        InteractiveCv original = DocumentFactory.populatedInteractiveCv("cv-123");

        InteractiveCv projectedCV = original.projectDocument();
        assertNull(projectedCV.getLanguage());
        assertNull(projectedCV.getProfilePicture());
        assertEquals(2, projectedCV.getTechnicalSkill().size());
        assertEquals(1, projectedCV.getEducation().size());
        log.info(projectedCV.getEducation().get(0).toString());
        log.info(projectedCV.getEducation().toString());
        assertEquals("Harvard", projectedCV.getEducation().get(0).getTrainingCenter());
    }
}