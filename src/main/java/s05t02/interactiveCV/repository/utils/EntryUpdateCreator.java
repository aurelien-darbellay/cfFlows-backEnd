package s05t02.interactiveCV.repository.utils;

import org.springframework.data.mongodb.core.query.Update;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

public interface EntryUpdateCreator {
    Update createEntryUpdate();

    Entry getEntry();
}

