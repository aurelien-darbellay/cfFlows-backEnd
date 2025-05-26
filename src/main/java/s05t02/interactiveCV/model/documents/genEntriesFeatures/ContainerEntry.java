package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class ContainerEntry extends Entry implements Positioned, Colored, Sized {
    private Position position;
    private String color;
    private Entry previousEntry;
    private Entry nextEntry;
    private double size;
}
