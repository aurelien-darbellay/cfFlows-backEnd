package s05t02.interactiveCV.repository.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;

@Getter
@RequiredArgsConstructor
public class ListEntryUpdateCreator implements EntryUpdateCreator {
    private final Entry entry;

    @Override
    public Update createEntryUpdate() {
        return new Update().push(entry.getKeyNameInDB(), entry);
    }
}
