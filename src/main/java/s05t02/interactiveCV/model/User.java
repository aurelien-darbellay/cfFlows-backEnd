package s05t02.interactiveCV.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.util.List;

@Getter
@Setter
@Document
@Builder
public class User {
    @Id
    private String id;
    private String username;
    private String hashedPassword;
    private List<InteractiveDocument> documents;
    @Builder.Default
    private Role role = Role.USER;

}
