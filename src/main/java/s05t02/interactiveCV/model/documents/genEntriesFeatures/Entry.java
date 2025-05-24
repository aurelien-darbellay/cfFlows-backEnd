package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entry {
    private boolean projected = true;
    private boolean highlighted = false;
}
