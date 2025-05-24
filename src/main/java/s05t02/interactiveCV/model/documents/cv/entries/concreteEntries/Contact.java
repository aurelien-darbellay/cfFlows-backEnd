package s05t02.interactiveCV.model.documents.cv.entries.concreteEntries;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ContainerEntry;

@Getter
@Setter
@Builder
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
