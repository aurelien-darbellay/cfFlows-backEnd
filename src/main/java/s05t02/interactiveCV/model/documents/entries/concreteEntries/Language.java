package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.HasId;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
public class Language extends ContainedEntry implements PointsToFileInCloud {
    public enum Level {
        CONVERSATIONAL, FLUENT, PROFICIENT, NATIVE, PROFESSIONAL
    }

    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private final String name;
    private final Level level;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;
}
