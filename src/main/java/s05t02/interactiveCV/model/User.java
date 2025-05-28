package s05t02.interactiveCV.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString(exclude = "hashedPassword")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class User {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed(unique = true)
    private String username;

    private String firstname;
    private String lastname;
    private String hashedPassword;
    @Builder.Default
    private List<InteractiveDocument> interactiveDocuments = new ArrayList<>();

    @Builder.Default
    private Role role = Role.USER;

}
