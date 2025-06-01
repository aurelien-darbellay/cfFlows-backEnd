package s05t02.interactiveCV.model.documents.cv;

import customCompileChecks.MatchesTypeName;
import lombok.*;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.Entry;
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
    private ListEntries<Language> language = ListEntries.<Language>builder().build();
    @Builder.Default
    private ListEntries<TechnicalSkill> technicalSkill = ListEntries.<TechnicalSkill>builder().build();
    @Builder.Default
    private ListEntries<SoftSkill> softSkill = ListEntries.<SoftSkill>builder().build();
    @Builder.Default
    private ListEntries<Portfolio> portfolio = ListEntries.<Portfolio>builder().build();

    public InteractiveCv() {

    }
    @Builder
    public InteractiveCv(String id, String title, Identity identity, Profession profession, ProfilePicture profilePicture, Contact contact, Summary summary, ListEntries<Education> education, ListEntries<Experience> experience, ListEntries<Language> language, ListEntries<TechnicalSkill> technicalSkill, ListEntries<SoftSkill> softSkill, ListEntries<Portfolio> portfolio) {
        this.id = id;
        this.title = title;
        this.identity = identity;
        this.profession = profession;
        this.profilePicture = profilePicture;
        this.contact = contact;
        this.summary = summary;
        this.education = education;
        this.experience = experience;
        this.language = language;
        this.technicalSkill = technicalSkill;
        this.softSkill = softSkill;
        this.portfolio = portfolio;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InteractiveCv projectDocument() {
        return this.toBuilder()
                .id(null)
                .identity(Entry.project(identity))
                .profession(Entry.project(profession))
                .profilePicture(Entry.project(profilePicture))
                .contact(Entry.project(contact))
                .summary(Entry.project(summary))
                .education(ListEntries.project(education))
                .experience(ListEntries.project(experience))
                .portfolio(ListEntries.project(portfolio))
                .language(ListEntries.project(language))
                .technicalSkill(ListEntries.project(technicalSkill))
                .softSkill(ListEntries.project(softSkill))
                .build();
    }

    @Override
    public HtmlTargetCoordinate getHtmlTargetCoordinate() {
        return new HtmlTargetCoordinate("cv", "cv-template");
    }

}
