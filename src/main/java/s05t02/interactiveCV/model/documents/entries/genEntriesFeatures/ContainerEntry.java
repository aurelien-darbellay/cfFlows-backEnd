package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.query.Update;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.Colored;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.Positioned;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.interfaces.Sized;

@Getter
@Setter
@SuperBuilder
public abstract class ContainerEntry extends Entry implements Positioned, Colored, Sized {
    private Position position;
    private String color;
    private String previousEntry;
    private String nextEntry;
    private String header;
    private double size;

    public ContainerEntry() {
        super();
    }

    @Override
    public Update createAddUpdate() {
        return new Update().set("interactiveDocuments.$." + this.getKeyNameInDB(), this);
    }

    @Override
    public Update createRemoveUpdate() {
        return new Update().unset("interactiveDocuments.$." + this.getKeyNameInDB());
    }
}
