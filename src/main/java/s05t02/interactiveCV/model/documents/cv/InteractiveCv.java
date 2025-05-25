package s05t02.interactiveCV.model.documents.cv;

import lombok.*;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ListEntries;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class InteractiveCv implements InteractiveDocument {
    @Builder.Default
    @EqualsAndHashCode.Include
    private String id = UUID.randomUUID().toString();
    private String title;
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

    @SuppressWarnings("unchecked")
    @Override
    public InteractiveCv getProjectedDocument() {
        return this.toBuilder()
                .id(null)
                .identity(identity.selfProject())
                .profession(profession.selfProject())
                .picture(picture.selfProject())
                .contact(contact.selfProject())
                .summary(summary.selfProject())
                .education(education.selfProject())
                .experiences(experiences.selfProject())
                .languages(languages.selfProject())
                .technicalSkills(technicalSkills.selfProject())
                .softSkills(softSkills.selfProject())
                .build();
    }
}
