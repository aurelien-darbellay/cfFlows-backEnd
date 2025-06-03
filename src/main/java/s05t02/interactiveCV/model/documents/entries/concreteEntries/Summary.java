package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@SuperBuilder
@ToString
@JsonTypeName("SUMMARY")
@EqualsAndHashCode(callSuper = false)
public class Summary extends ContainerEntry {
    @JsonCreator
    @PersistenceCreator
    public Summary() {
        super();
    }

    private String title;
    private String text;
}
