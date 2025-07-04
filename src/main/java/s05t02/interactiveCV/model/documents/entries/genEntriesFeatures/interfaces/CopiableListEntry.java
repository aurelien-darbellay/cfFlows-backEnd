package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces;

import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ListEntries;

public interface CopiableListEntry {
    <T extends Entry, L extends ListEntries<T>> L getCopy();
}
