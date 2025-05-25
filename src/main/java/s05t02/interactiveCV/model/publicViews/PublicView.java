package s05t02.interactiveCV.model.publicViews;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import s05t02.interactiveCV.model.documents.InteractiveDocument;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Document
@Builder
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PublicView {
    @EqualsAndHashCode.Include
    @Id
    private final String id;
    private final String username;
    private final InteractiveDocument document;
    @Builder.Default
    private final LocalDate dateCreation = LocalDate.now();
    @Builder.Default
    private final LocalDate dateExpiration = LocalDate.now().plus(Period.ofDays(200));
}
