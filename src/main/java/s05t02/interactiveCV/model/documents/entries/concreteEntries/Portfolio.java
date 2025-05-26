package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

@Getter
@Setter
@SuperBuilder
@ToString
public class Portfolio extends Entry {
    private String projectName;
    private String projectUrl;
}
