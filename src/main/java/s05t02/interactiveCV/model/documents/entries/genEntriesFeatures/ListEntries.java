package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.*;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.ListLike;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString
@RequiredArgsConstructor
public class ListEntries<T extends Entry> extends ContainerEntry implements ListLike<T> {
    @Delegate
    @Builder.Default
    private List<T> entries = new ArrayList<>();
    static private Logger log = LoggerFactory.getLogger(ListEntries.class);

    public static <T extends Entry> ListEntries<T> of(List<T> collection, Class<T> clazz) {
        ListEntries<T> newList = ListEntries.<T>builder().build();
        newList.addAll(collection);
        return newList;
    }

    public static <T extends Entry> ListEntries<T> project(ListEntries<T> listEntries) {
        if (listEntries == null) return null;
        log.atDebug().log("ListEntries position is {}", listEntries.getPosition());
        if (!listEntries.isProjected()) {
            return null;
        }
        List<T> filteredEntries = listEntries.getEntries().stream().filter(Entry::isProjected).toList();
        if (filteredEntries.isEmpty()) return null;
        listEntries.setEntries(filteredEntries);
        return listEntries;
    }


    @Override
    public String getKeyNameInDB() {
        if (this.isEmpty()) return null;
        return this.get(0).getKeyNameInDB();
    }

}
