package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Language extends Entry implements PointsToFileInCloud {
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
