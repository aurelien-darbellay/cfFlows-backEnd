package s05t02.interactiveCV.repository;

import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import s05t02.interactiveCV.model.User;
import s05t02.interactiveCV.model.documents.InteractiveDocument;
import s05t02.interactiveCV.model.documents.cv.InteractiveCv;
import s05t02.interactiveCV.model.documents.entries.concreteEntries.*;
import s05t02.interactiveCV.model.documents.entries.genEntriesFeatures.ListEntries;
import s05t02.interactiveCV.service.cloud.CloudinaryMetaData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
class UserRepositoryTest {

    private final static Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @Autowired
    UserRepository userRepository;

    @Test
    void saveUserWithNoDocument() {
        User newUser = User.builder().id("6831fa6dcc1ac71fbe347d1d").username("aure").hashedPassword("777jlllope2").build();
        Mono<User> userMono = userRepository.save(newUser);
        StepVerifier.create(userMono)
                .consumeNextWith(user -> log.info("Username : {}", user.getUsername()))
                .verifyComplete();
        Mono<Void> mono = userRepository.deleteByUsername("aure");
        StepVerifier.create(mono).verifyComplete();
    }

    @Test
    void savingFailWhenDuplicateUserName() {
        User newUser = User.builder().username("duplicate").id("1").hashedPassword("jpl").build();
        User duplicateUser = User.builder().username("duplicate").id("2").hashedPassword("jpl").build();
        userRepository.save(newUser).block();
        Mono<User> duplicateMono = userRepository.save(duplicateUser);
        StepVerifier.create(duplicateMono)
                .expectError(DuplicateKeyException.class);
    }

    @Test
    void ifIdIsSame_thenNoDuplicateKeyError() {
        User newUser = User.builder().username("newDuplicate").id("3").hashedPassword("jpl").build();
        User duplicateUser = User.builder().username("newDuplicate").id("3").hashedPassword("exito").build();
        userRepository.save(newUser).block();
        Mono<User> duplicateMono = userRepository.save(duplicateUser);
        StepVerifier.create(duplicateMono)
                .expectNext(duplicateUser)
                .verifyComplete();
    }

    @Test
    void interactiveCvInitializeListByDefault() {
        InteractiveCv cv = InteractiveCv.builder().build();
        User newUser = User.builder().username("leo").id("4").hashedPassword("jpl").interactiveDocuments(List.of(cv)).build();
        Mono<User> userMono = userRepository.save(newUser);
        StepVerifier.create(userMono)
                .consumeNextWith(user ->
                {
                    InteractiveDocument retrievedCv = user.getInteractiveDocuments().get(0);
                    assertInstanceOf(InteractiveCv.class, retrievedCv);
                    ListEntries<Education> education = ((InteractiveCv) retrievedCv).getEducation();
                    assertNotNull(education);
                    ListEntries<Experience> experiences = ((InteractiveCv) retrievedCv).getExperience();
                    assertNotNull(experiences);
                    ListEntries<SoftSkill> softSkills = ((InteractiveCv) retrievedCv).getSoftSkill();
                    assertNotNull(softSkills);
                    ListEntries<TechnicalSkill> technicalSkills = ((InteractiveCv) retrievedCv).getTechnicalSkill();
                    assertNotNull(technicalSkills);
                    ListEntries<Language> languages = ((InteractiveCv) retrievedCv).getLanguage();
                    assertNotNull(languages);
                }).verifyComplete();
    }

    @Test
    void buildAndSaveUserWithCompleteCV() {
        Summary summary = Summary.builder().title("About me").text("This is a very short text about me").build();
        ProfilePicture picture = ProfilePicture.builder().cloudMetaData(new CloudinaryMetaData("good", "http://super")).shape(ProfilePicture.Shape.ROUND).size(100).build();
        Identity identity = Identity.builder().names(List.of("Aurelien")).lastNames(List.of("Darbellay", "Courvoisier")).build();
        Profession profession = Profession.builder().generalTitle("Developer").specificTitle("Java Spring Boot").build();
        ListEntries<Education> education = ListEntries.<Education>builder().build();
        education.add(Education.builder().title("Math").graduationYear(2002).build());
        ListEntries<Experience> experiences = ListEntries.<Experience>builder().build();
        experiences.add(Experience.builder().description("Super goog").nameCompany("Mine").build());
        ListEntries<Language> languages = ListEntries.<Language>builder().build();
        languages.add(Language.builder().name("French").level(Language.Level.NATIVE).build());
        ListEntries<SoftSkill> softSkills = ListEntries.<SoftSkill>builder().build();
        softSkills.add(SoftSkill.builder().keyWords("Conflict Resolution").build());
        ListEntries<TechnicalSkill> technicalSkills = ListEntries.<TechnicalSkill>builder().build();
        technicalSkills.add(TechnicalSkill.builder().keyWords("NodeJs").build());
        InteractiveCv cv = InteractiveCv.builder()
                .identity(identity).profilePicture(picture)
                .summary(summary).profession(profession)
                .education(education).experience(experiences).language(languages).softSkill(softSkills).technicalSkill(technicalSkills).build();
        User user = User.builder().username("testCV").hashedPassword("testPass").interactiveDocuments(List.of(cv)).id("683209dd65a9dc3bb63f5097").build();
        userRepository.save(user).block();
    }
}