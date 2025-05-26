package s05t02.interactiveCV.testClasses;

import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ListEntries;

import java.util.List;

public class DocumentFactory {
    public static InteractiveCv populatedInteractiveCv(String id) {
        return InteractiveCv.builder()
                .id(id)
                .identity(Identity.builder().projected(true).names(List.of("John Doe")).build())
                .profession(Profession.builder().projected(true).generalTitle("Software Engineer").build())
                .picture(ProfilePicture.builder().projected(false).urlPicture("https://example.com/pic.jpg").build())
                .contact(Contact.builder().projected(true).email("john@example.com").build())
                .summary(Summary.builder().projected(false).text("Summary that should be excluded.").build())
                .education(ListEntries.<Education>builder()
                        .entries(List.of(
                                Education.builder().projected(true).trainingCenter("Harvard").build(),
                                Education.builder().projected(false).trainingCenter("Should be filtered").build()
                        )).projected(true).build())
                .experiences(ListEntries.<Experience>builder()
                        .entries(List.of(
                                Experience.builder().projected(true).nameCompany("Google").build()
                        )).projected(true).build())
                .languages(ListEntries.<Language>builder()
                        .entries(List.of(
                                Language.builder().projected(false).name("Latin").build()
                        )).projected(true).build())
                .technicalSkills(ListEntries.<TechnicalSkill>builder()
                        .entries(List.of(
                                TechnicalSkill.builder().projected(true).keyWords("Java").build(),
                                TechnicalSkill.builder().projected(true).keyWords("JS").build()
                        )).projected(true).build())
                .softSkills(ListEntries.<SoftSkill>builder()
                        .entries(List.of(
                                SoftSkill.builder().projected(true).keyWords("Communication").build()

                        )).projected(false).build())
                .build();
    }
}
