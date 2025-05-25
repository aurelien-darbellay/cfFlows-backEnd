package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@SuperBuilder
@ToString
public class Summary extends ContainerEntry {
    private String title;
    private String text;
}
