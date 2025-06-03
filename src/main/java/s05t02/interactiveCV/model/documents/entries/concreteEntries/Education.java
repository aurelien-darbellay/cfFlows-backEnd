package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
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
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Education extends ContainedEntry implements PointsToFileInCloud {

    @JsonCreator
    @PersistenceCreator
    public Education(String id) {
        super();
        this.id = id == null ? UUID.randomUUID().toString() : id;
    }

    @EqualsAndHashCode.Include
    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String trainingCenter;
    private int graduationYear;
    private String comments;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;

}
