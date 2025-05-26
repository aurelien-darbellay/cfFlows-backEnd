package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;
import s05t02.interactiveCV.service.cloud.CloudinaryMetaData;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Education extends Entry implements PointsToFileInCloud {

    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private String title;
    private String trainingCenter;
    private int graduationYear;
    private String comments;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;

}
