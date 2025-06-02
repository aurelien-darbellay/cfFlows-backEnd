package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.query.Update;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.HasId;

import java.util.Collections;

@SuperBuilder
public abstract class ContainedEntry extends Entry implements HasId {
    public ContainedEntry() {
        super();
    }

    @Override
    public Update createAddUpdate() {
        return new Update().push("interactiveDocuments.$." + this.getKeyNameInDB() + ".entries", this);
    }

    @Override
    public Update createRemoveUpdate() {
        return new Update().pull("interactiveDocuments.$." + this.getKeyNameInDB() + ".entries", Collections.singletonMap("_id", this.getId()));
    }
}
