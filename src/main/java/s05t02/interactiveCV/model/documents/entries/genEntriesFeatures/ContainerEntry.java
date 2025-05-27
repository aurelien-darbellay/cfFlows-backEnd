package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class ContainerEntry extends Entry implements Positioned, Colored, Sized {
    private Position position;
    private String color;
    private Entry previousEntry;
    private Entry nextEntry;
    private double size;
}
