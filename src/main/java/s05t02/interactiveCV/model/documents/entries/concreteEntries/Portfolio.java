package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
public class Portfolio extends ContainedEntry {

    @JsonCreator
    @PersistenceCreator
    public Portfolio(String id) {
        super();
        this.id = id == null ? UUID.randomUUID().toString() : id;
    }

    @Builder.Default
    final String id = UUID.randomUUID().toString();
    private String projectName;
    private String projectUrl;
}
