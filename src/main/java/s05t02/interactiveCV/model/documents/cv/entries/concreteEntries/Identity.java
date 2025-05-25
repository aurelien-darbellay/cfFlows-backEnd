package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ContainerEntry;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
public class Identity extends ContainerEntry {
    @Builder.Default
    private List<String> names = new ArrayList<>();
    @Builder.Default
    private List<String> lastNames = new ArrayList<>();
}
