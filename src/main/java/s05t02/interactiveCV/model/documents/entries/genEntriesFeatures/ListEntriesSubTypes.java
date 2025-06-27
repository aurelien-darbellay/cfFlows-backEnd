package s05t02.interactiveCV.model.documents.entries.genEntriesFeatures;

import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;

public class ListEntriesSubTypes {
    public static class EducationListEntries extends ListEntries<Education> {
        @Override
        public String getKeyNameInDB() {
            return "education";
        }
    }

    public static class ExperienceListEntries extends ListEntries<Experience> {
        public String getKeyNameInDB() {
            return "experience";
        }
    }

    public static class LanguageListEntries extends ListEntries<Language> {
        public String getKeyNameInDB() {
            return "language";
        }
    }

    public static class PortfolioListEntries extends ListEntries<Portfolio> {
        public String getKeyNameInDB() {
            return "portfolio";
        }
    }

    public static class TechnicalSkillListEntries extends ListEntries<TechnicalSkill> {
        public String getKeyNameInDB() {
            return "technicalSkill";
        }
    }

    public static class SoftSkillListEntries extends ListEntries<SoftSkill> {
        public String getKeyNameInDB() {
            return "softSkill";
        }
    }
}
