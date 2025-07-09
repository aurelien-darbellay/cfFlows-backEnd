package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;
import s05t02.interactiveCV.service.cloud.CloudMetaData;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfilePicture extends ContainerEntry {
    public enum Shape {
        SQUARE, RECTANGLE, STAR, ROUND, MATCH
    }

    @JsonCreator
    @PersistenceCreator
    public ProfilePicture(CloudMetaData cloudMetaData) {
        super();
        this.cloudMetaData = cloudMetaData;
    }

    private final CloudMetaData cloudMetaData;
    @Builder.Default
    private Shape shape = Shape.ROUND;

}
