package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;

public class ListEntriesSubTypes {
    public static class ListEntriesEducation extends ListEntries<Education> {
        public String getKeyNameInDB() {
            return "education";
        }

    }

    public static class ListEntriesExperience extends ListEntries<Experience> {
        public String getKeyNameInDB() {
            return "experience";
        }
    }

    public static class ListEntriesLanguage extends ListEntries<Language> {
        public String getKeyNameInDB() {
            return "language";
        }
    }

    public static class ListEntriesPortfolio extends ListEntries<Portfolio> {
        public String getKeyNameInDB() {
            return "portfolio";
        }

    }

    public static class ListEntriesTechnicalSkill extends ListEntries<TechnicalSkill> {
        public String getKeyNameInDB() {
            return "technicalSkill";
        }

    }

    public static class ListEntriesSoftSkill extends ListEntries<SoftSkill> {
        public String getKeyNameInDB() {
            return "softSkill";
        }
    }
}
