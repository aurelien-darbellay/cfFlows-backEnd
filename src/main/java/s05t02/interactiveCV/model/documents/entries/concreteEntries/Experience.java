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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Experience extends ContainedEntry implements PointsToFileInCloud {

    @JsonCreator
    @PersistenceCreator
    public Experience(String id) {
        super();
        this.id = id == null ? UUID.randomUUID().toString() : id;
    }

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
