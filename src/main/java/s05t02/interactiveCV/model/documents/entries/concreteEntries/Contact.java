package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.PersistenceCreator;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ContainerEntry;


@Getter
@Setter
@SuperBuilder
@ToString
@RequiredArgsConstructor(onConstructor = @__(@PersistenceCreator))
public class Contact extends ContainerEntry {
    private String phoneNumber;
    private String email;
    private String linkedInAccount;
    private String gitHubAccount;
    private String instagramAccount;
    private String facebookAccount;
    private String cityOfResidence;
    private int zipCode;
}
