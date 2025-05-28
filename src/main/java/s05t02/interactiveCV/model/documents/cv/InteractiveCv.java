package s05t02.interactiveCV.model.documents.cv;

import customCompileChecks.MatchesTypeName;
import lombok.*;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.entries.EntryType;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ListEntries;

import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@MatchesTypeName
public class InteractiveCv implements InteractiveDocument {
    @MatchesTypeName(ignore = true)
    @Builder.Default
    @EqualsAndHashCode.Include
    private String id = UUID.randomUUID().toString();
    @MatchesTypeName(ignore = true)
    private String title;
    private Identity identity;
    private Profession profession;
    private ProfilePicture profilePicture;
    private Contact contact;
    private Summary summary;
    @Builder.Default
    private ListEntries<Education> education = ListEntries.<Education>builder().build();
    @Builder.Default
    private ListEntries<Experience> experience = ListEntries.<Experience>builder().build();
    @Builder.Default
    private ListEntries<Language> language =ListEntries.<Language>builder().build();
    @Builder.Default
    private ListEntries<TechnicalSkill> technicalSkill = ListEntries.<TechnicalSkill>builder().build();
    @Builder.Default
    private ListEntries<SoftSkill> softSkill = ListEntries.<SoftSkill>builder().build();
    @Builder.Default
    private ListEntries<Portfolio> portfolio = ListEntries.<Portfolio>builder().build();

    @SuppressWarnings("unchecked")
    @Override
    public InteractiveCv getProjectedDocument() {
        return this.toBuilder()
                .id(null)
                .identity(identity.selfProject())
                .profession(profession.selfProject())
                .profilePicture(profilePicture.selfProject())
                .contact(contact.selfProject())
                .summary(summary.selfProject())
                .education(education.selfProject())
                .experience(experience.selfProject())
                .portfolio(portfolio.selfProject())
                .language(language.selfProject())
                .technicalSkill(technicalSkill.selfProject())
                .softSkill(softSkill.selfProject())
                .build();
    }

    @Override
    public HtmlTargetCoordinate getHtmlTargetCoordinate() {
        return new HtmlTargetCoordinate("cv", "cv-template");
    }
}
