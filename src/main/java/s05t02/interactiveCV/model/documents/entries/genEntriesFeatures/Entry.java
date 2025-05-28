package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.repository.customRepos.updatesCreators.CreatesMongoDbUpdate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Entry implements CreatesMongoDbUpdate {
    private boolean projected;
    private boolean highlighted;

    @SuppressWarnings("unchecked")
    public <T extends Entry> T selfProject() {
        return this.projected ? (T) this : null;
    }

    public String getKeyNameInDB() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
