package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;
import s05t02.interactiveCV.repository.customRepos.updatesCreators.CreatesMongoDbUpdate;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Identity.class, name = "IDENTITY"),
        @JsonSubTypes.Type(value = Summary.class, name = "SUMMARY"),
        @JsonSubTypes.Type(value = Education.class, name = "EDUCATION"),
        @JsonSubTypes.Type(value = Experience.class, name = "EXPERIENCE"),
        @JsonSubTypes.Type(value = Contact.class, name = "CONTACT"),
        @JsonSubTypes.Type(value = Portfolio.class, name = "PORTFOLIO"),
        @JsonSubTypes.Type(value = Profession.class, name = "PROFESSION"),
        @JsonSubTypes.Type(value = ProfilePicture.class, name = "PROFILE_PICTURE"),
        @JsonSubTypes.Type(value = SoftSkill.class, name = "SOFT_SKILL"),
        @JsonSubTypes.Type(value = TechnicalSkill.class, name = "TECHNICAL_SKILL"),
        @JsonSubTypes.Type(value = Language.class, name = "LANGUAGE"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesEducation.class, name = "LIST_EDUCATION"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesExperience.class, name = "LIST_EXPERIENCE"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesLanguage.class, name = "LIST_LANGUAGE"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesPortfolio.class, name = "LIST_PORTFOLIO"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesSoftSkill.class, name = "LIST_SOFT_SKILL"),
        @JsonSubTypes.Type(value = ListEntriesSubTypes.ListEntriesTechnicalSkill.class, name = "LIST_TECHNICAL_SKILL"),

})
public abstract class Entry implements CreatesMongoDbUpdate {
    private boolean projected;
    private boolean highlighted;

    public Entry() {
        this.projected = true;
        this.highlighted = false;
    }

    public static <T extends Entry> T project(T entry) {
        return (entry == null || !entry.isProjected()) ? null : entry;
    }


    public String getKeyNameInDB() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
