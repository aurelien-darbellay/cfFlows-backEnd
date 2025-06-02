package s05t02.interactiveCV.model;

import lombok.Getter;
import org.springframework.stereotype.Component;
import s05t02.interactiveCV.model.documents.InteractiveDocumentType;
import s05t02.interactiveCV.model.documents.entries.EntryType;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.Language;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class TypesConfig {
    private final List<String> documentTypes = Arrays.stream(InteractiveDocumentType.values()).map(InteractiveDocumentType::name).toList();
    private final List<String> entryTypes = Arrays.stream(EntryType.values()).map(EntryType::name).toList();
    private final List<String> roles = Arrays.stream(Role.values()).map(Role::name).toList();
    private final List<String> linguisticLevels = Arrays.stream(Language.Level.values()).map(Language.Level::name).toList();
}
