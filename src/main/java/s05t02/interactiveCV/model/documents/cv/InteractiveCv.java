package s05t02.interactiveCV.model.documents.cv;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.genEntriesFeatures.ListEntries;

@Getter
@Setter
@Builder
public class InteractiveCv implements InteractiveDocument {
    private Identity identity;
    private Profession profession;
    private ProfilePicture picture;
    private Contact contact;
    private Summary summary;
    private ListEntries<Education> education;
    private ListEntries<Experience> experiences;
    private ListEntries<Language> languages;
    private ListEntries<TechnicalSkill> technicalSkills;
    private ListEntries<SoftSkill> softSkills;

}
