package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@SuperBuilder
@ToString
public class Summary extends ContainerEntry {
    private String title;
    private String text;
}
