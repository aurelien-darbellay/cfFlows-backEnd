package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ContainerEntry extends Entry implements Positioned, Colored {
    private Position position;
    private String color;
    private Entry previousEntry;
    private Entry nextEntry;
}
