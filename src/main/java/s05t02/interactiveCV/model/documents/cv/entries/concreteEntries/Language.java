package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.Entry;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Language extends Entry {
    public enum Level {
        CONVERSATIONAL, FLUENT, PROFICIENT, NATIVE, PROFESSIONAL
    }

    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private final String name;
    private final Level level;
    private String urlCertificate;
    private String nameCertificate;
}
