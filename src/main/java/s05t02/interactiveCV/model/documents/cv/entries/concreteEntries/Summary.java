package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@Builder
public class Summary extends ContainerEntry {
    private String title;
    private String text;
}
