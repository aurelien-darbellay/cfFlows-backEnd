package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonTypeName;
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
public class TechnicalSkill extends ContainedEntry {
    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private String keyWords;
}
