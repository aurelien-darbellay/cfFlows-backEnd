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

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Experience extends ContainedEntry implements PointsToFileInCloud {

    @JsonCreator
    @PersistenceCreator
    public Experience() {
        super();
    }

    private String role;
    private String nameCompany;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String keywords;
    private String nameLink;
    private String linkUrl;
    private String cloudDocumentName;
    private CloudMetaData documentCloudMetadata;

}
