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
public class Profession extends ContainerEntry {
    private String generalTitle;
    private String specificTitle;
}
