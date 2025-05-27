package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Entry {
    private boolean projected;
    private boolean highlighted;

    @SuppressWarnings("unchecked")
    public <T extends Entry> T selfProject() {
        return this.projected ? (T) this : null;
    }
}
