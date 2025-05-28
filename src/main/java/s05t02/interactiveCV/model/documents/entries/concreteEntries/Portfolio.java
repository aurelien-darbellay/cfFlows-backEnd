package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.HasId;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
public class Portfolio extends ContainedEntry {
    @Builder.Default
    final String id = UUID.randomUUID().toString();
    private String projectName;
    private String projectUrl;
}
