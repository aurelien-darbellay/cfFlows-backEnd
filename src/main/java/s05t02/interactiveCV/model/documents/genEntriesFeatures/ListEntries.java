package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.cv.entries.CvEntryType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@ToString
public class ListEntries<T extends Entry> extends ContainerEntry implements List<T> {
    private CvEntryType type;
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
        if (!this.isProjected()) return null;
        Class<T> clazz = (Class<T>) entries.get(0).getClass();
        return of(entries.stream().filter(Entry::isProjected).toList(), clazz);
    }

}
