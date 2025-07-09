package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Language extends ContainedEntry implements PointsToFileInCloud {
    public enum Level {
        CONVERSATIONAL, FLUENT, PROFICIENT, NATIVE, PROFESSIONAL
    }

    @JsonCreator
    @PersistenceCreator
    public Language(String name, Level level) {
        super();
        this.name = name;
        this.level = level;
    }

    private final String name;
    private final Level level;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;
}
