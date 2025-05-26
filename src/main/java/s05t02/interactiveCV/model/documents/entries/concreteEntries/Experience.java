package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.PointsToFileInCloud;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Experience extends Entry implements PointsToFileInCloud {
    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private String position;
    private String nameCompany;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private List<String> keywords;
    private String nameLink;
    private String linkUrl;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;

}
