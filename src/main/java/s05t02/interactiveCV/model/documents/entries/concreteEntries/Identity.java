package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@Jacksonized
@ToString
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
public class Identity extends ContainerEntry {
    @Builder.Default
    private List<String> names = new ArrayList<>();
    @Builder.Default
    private List<String> lastNames = new ArrayList<>();
}
