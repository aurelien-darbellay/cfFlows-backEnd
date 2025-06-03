package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
public class ProfilePicture extends ContainerEntry {
    public enum Shape {
        SQUARE, TRIANGLE, RECTANGLE, STAR, ROUND
    }

    @JsonCreator
    @PersistenceCreator
    public ProfilePicture(String urlPicture) {
        super();
        this.urlPicture = urlPicture;
    }

    private final String urlPicture;
    @Builder.Default
    private Shape shape = Shape.ROUND;

}
