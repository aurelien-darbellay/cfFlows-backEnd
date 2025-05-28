package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.*;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.EntryType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
@RequiredArgsConstructor
public class ListEntries<T extends Entry> extends ContainerEntry implements List<T> {
    @Delegate
    @Builder.Default
    private List<T> entries = new ArrayList<>();

    public static <T extends Entry> ListEntries<T> of(List<T> collection, Class<T> clazz) {
        ListEntries<T> newList = ListEntries.<T>builder().build();
        newList.addAll(collection);
        return newList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListEntries<T> selfProject() {
        if (!this.isProjected() || entries.isEmpty()) return null;
        Class<T> clazz = (Class<T>) entries.get(0).getClass();
        return of(entries.stream().filter(Entry::isProjected).toList(), clazz);
    }
    
    @Override
    public String getKeyNameInDB(){
        if (this.isEmpty()) return null;
        return this.get(0).getKeyNameInDB();
    }


}
