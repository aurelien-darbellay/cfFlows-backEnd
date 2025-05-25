package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class Entry {
    private boolean projected;
    private boolean highlighted;

    @SuppressWarnings("unchecked")
    public <T extends Entry> T selfProject() {
        return this.projected ? (T) this : null;
    }
}
