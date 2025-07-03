package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.*;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
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

    public static <T extends Entry> ListEntries<T> of(List<T> collection, Class<T> clazz) {
        ListEntries<T> newList = ListEntries.<T>builder().build();
        newList.addAll(collection);
        return newList;
    }

    public static <T extends Entry> ListEntries<T> project(ListEntries<T> listEntries) {
        if (listEntries == null) return null;
        List<T> entries = listEntries.getEntries();
        if (!listEntries.isProjected() || entries.isEmpty()) {
            return listEntries.toBuilder().entries(new ArrayList<>()).build();
        }
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) entries.get(0).getClass();
        return ListEntries.of(entries.stream().filter(Entry::isProjected).toList(), clazz);
    }

    @Override
    public String getKeyNameInDB() {
        if (this.isEmpty()) return null;
        return this.get(0).getKeyNameInDB();
    }

}
