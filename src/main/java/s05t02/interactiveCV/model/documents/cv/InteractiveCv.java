package s05t02.interactiveCV.model.documents.cv;

import lombok.*;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ListEntries;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class InteractiveCv extends InteractiveDocument {
    @EqualsAndHashCode.Include
    @Builder.Default
    private final String id = UUID.randomUUID().toString();
    private Identity identity;
    private Profession profession;
    private ProfilePicture picture;
    private Contact contact;
    private Summary summary;
    @Builder.Default
    private ListEntries<Education> education = ListEntries.<Education>builder().build();
    @Builder.Default
    private ListEntries<Experience> experiences = ListEntries.<Experience>builder().build();
    @Builder.Default
    private ListEntries<Language> languages = ListEntries.<Language>builder().build();
    @Builder.Default
    private ListEntries<TechnicalSkill> technicalSkills = ListEntries.<TechnicalSkill>builder().build();
    @Builder.Default
    private ListEntries<SoftSkill> softSkills = ListEntries.<SoftSkill>builder().build();

    @Override
    public String getId() {
        return this.id;
    }
}
