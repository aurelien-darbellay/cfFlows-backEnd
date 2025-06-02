package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Language extends ContainedEntry implements PointsToFileInCloud {
    public enum Level {
        CONVERSATIONAL, FLUENT, PROFICIENT, NATIVE, PROFESSIONAL
    }

    @JsonCreator
    @PersistenceCreator
    public Language(String id, String name, Level level) {
        super();
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.level = level;
    }

    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private final String name;
    private final Level level;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;
}
