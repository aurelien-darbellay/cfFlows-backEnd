package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.repository.customRepos.updatesCreators.CreatesMongoDbUpdate;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class Entry implements CreatesMongoDbUpdate {
    private boolean projected;
    private boolean highlighted;

    public static <T extends Entry> T project(T entry) {
        return (entry == null || !entry.isProjected()) ? null : entry;
    }


    public String getKeyNameInDB() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
