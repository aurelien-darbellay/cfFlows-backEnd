package s05t02.interactiveCV.model.documents.entries;

import lombok.Getter;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;

import java.util.List;

@Getter
public enum EntryType {
    SUMMARY("Summary", "3/5 lines max. Packed into one paragraph.", Summary.class, List.of(InteractiveCv.class),1),
    PROFESSION("Profession", "4/5 words. Rendered as a one-liner.", Profession.class, List.of(InteractiveCv.class),1),
    IDENTITY("Identity", "First Names, Last Names", Identity.class, List.of(InteractiveCv.class),1),
    PROFILE_PICTURE("Profile Picture", "Profile picture", ProfilePicture.class, List.of(InteractiveCv.class),1),
    LANGUAGE("Language", "Two fields - name and level", Language.class, List.of(InteractiveCv.class),10),
    EXPERIENCE("Experience", "Position, Name Company, Date Beginning, Date End, Description (two-lines max), Keywords, Link (name + value), File",Experience.class, List.of(InteractiveCv.class),50),
    EDUCATION("Education", "Title, Training Center, Year End, Comments", Education.class, List.of(InteractiveCv.class),20),
    CONTACT("Contact", "Phone, E-mail, LinkedIn, City of Residence, ZipCode,GitHub, Instagram,Facebook", Contact.class, List.of(InteractiveCv.class),1),
    TECH_SKILL("Technical Skill", "Key Words",TechnicalSkill.class, List.of(InteractiveCv.class),100),
    SOFT_SKILL("Soft Skill", "Key Words", SoftSkill.class, List.of(InteractiveCv.class),20),
    PORTFOLIO("Portfolio","Link towards deployed project", Portfolio.class,List.of(InteractiveCv.class),5);

    private final String simpleName;
    private final String description;
    private final List<Class<? extends InteractiveDocument>> docTypesUsingIt;
    private final int maxItemsOfType;
    private final Class<?> clazz;

    EntryType(String simpleName, String description, Class<?> clazz, List<Class<? extends InteractiveDocument>> docTypesUsingIt, int maxItemsOfType) {
        this.simpleName = simpleName;
        this.description = description;
        this.maxItemsOfType = maxItemsOfType;
        this.docTypesUsingIt = docTypesUsingIt;
        this.clazz = clazz;
    }

}
