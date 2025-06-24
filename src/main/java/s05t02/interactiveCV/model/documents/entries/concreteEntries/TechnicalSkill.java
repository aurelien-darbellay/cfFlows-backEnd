package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainedEntry;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TechnicalSkill extends ContainedEntry {
    @JsonCreator
    @PersistenceCreator
    public TechnicalSkill(String id) {
        super();
        this.id = id == null ? UUID.randomUUID().toString() : id;
    }

    @EqualsAndHashCode.Include
    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private String keyWords;
}
