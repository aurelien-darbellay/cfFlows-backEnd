package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@SuperBuilder
@ToString
public class ProfilePicture extends ContainerEntry {
    public enum Shape {
        SQUARE, TRIANGLE, RECTANGLE, STAR, ROUND
    }

    private final String urlPicture;
    @Builder.Default
    private Shape shape = Shape.ROUND;

}
