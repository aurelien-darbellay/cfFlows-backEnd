package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
public class Identity extends ContainerEntry {
    @Builder.Default
    private List<String> names = new ArrayList<>();
    @Builder.Default
    private List<String> lastNames = new ArrayList<>();
}
