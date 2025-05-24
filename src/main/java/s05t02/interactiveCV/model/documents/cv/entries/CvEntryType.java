package s05t02.interactiveCV.model.documents.cv.entries;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CvEntryType {
    SUMMARY("Summary", "3/5 lines max. Packed into one paragraph.", 1),
    PROFESSION("Profession", "4/5 words. Rendered as a one-liner.", 1),
    IDENTITY("Identity", "First Names, Last Names", 1),
    PICTURE("ProfilePicture", "Profile picture", 1),
    LANGUAGE("Language", "Two fields - name and level", 10),
    EXPERIENCE("Experience", "Position, Name Company, Date Beginning, Date End, Description (two-lines max), Keywords, Link (name + value), File", 50),
    EDUCATION("Education", "Title, Training Center, Year End, Comments", 20),
    CONTACT("Contact", "Phone, E-mail, LinkedIn, City of Residence, ZipCode,GitHub, Instagram,Facebook", 1),
    TECH_SKILL("TechnicalSkill", "Key Words", 100),
    SOFT_SKILL("SoftSkill", "Key Words", 20);

    static public CvEntryType getTypeFromSimpleName(String simpleName) {
        return Arrays.stream(CvEntryType.values()).filter(cvEntryType -> cvEntryType.getSimpleName().equals(simpleName)).findFirst().orElse(null);
    }

    private final String simpleName;
    private final String description;
    private final int maxItemsOfType;

    CvEntryType(String simpleName, String description, int maxItemsOfType) {
        this.simpleName = simpleName;
        this.description = description;
        this.maxItemsOfType = maxItemsOfType;
    }

}
