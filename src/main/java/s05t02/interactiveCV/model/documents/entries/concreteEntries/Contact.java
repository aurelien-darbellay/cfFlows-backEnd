package s05t02.interactiveCV.model.documents.entries.concreteEntries;

import com.fasterxml.jackson.annotation.JsonCreator;
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
@EqualsAndHashCode(callSuper = false)
public class Contact extends ContainerEntry {

    @JsonCreator
    @PersistenceCreator
    public Contact() {
        super();
    }

    private String phoneNumber;
    private String email;
    private String linkedInAccount;
    private String gitHubAccount;
    private String instagramAccount;
    private String facebookAccount;
    private String cityOfResidence;
    private int zipCode;
}
