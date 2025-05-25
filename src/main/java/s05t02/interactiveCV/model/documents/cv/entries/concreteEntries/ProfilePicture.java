package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ContainerEntry;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.Sized;

@Getter
@Setter
@SuperBuilder
@ToString
public class ProfilePicture extends ContainerEntry implements Sized {
    public enum Shape {
        SQUARE, TRIANGLE, RECTANGLE, STAR, ROUND
    }

    private final String urlPicture;
    @Builder.Default
    private Shape shape = Shape.ROUND;
    @Builder.Default
    private double size = 10;

}
