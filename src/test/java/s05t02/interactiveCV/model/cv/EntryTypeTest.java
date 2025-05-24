package s05t02.interactiveCV.model.cv;

import org.junit.jupiter.api.Test;
import s05t02.interactiveCV.model.documents.cv.entries.CvEntryType;

import static org.junit.jupiter.api.Assertions.assertSame;

class EntryTypeTest {

    @Test
    void getTypeFromSimpleName() {
        CvEntryType type = CvEntryType.getTypeFromSimpleName("Summary");
        assertSame(CvEntryType.SUMMARY, type);
    }
}