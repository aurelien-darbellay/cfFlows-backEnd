package s05t02.interactiveCV.model.documents.genEntriesFeatures;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import s05t02.interactiveCV.model.documents.cv.entries.CvEntryType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class ListEntries<T extends Entry> extends ContainerEntry implements List<T> {
    private CvEntryType type;
    @Delegate
    @Builder.Default
    private List<T> entries = new ArrayList<>();

}
