package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {
    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    TestCenterRepository testCenterRepository;
    @Autowired
    CandidateEvaluationGridRepository evaluationGridRepository;

    @BeforeEach
    void restoreRepoToDefault() {
        candidateRepository.deleteAll();
    }

    @Test
    void testFindAllByTestCenterEntityCode() {

       // given
       TestCenterEntity centreGrenoble = TestCenterEntity
               .builder()
               .code(TestCenterCode.GRE)
               .university("Université Grenoble Alpes")
               .city("Grenoble")
               .build();

       TestCenterEntity centreDijon = TestCenterEntity
               .builder()
               .code(TestCenterCode.DIJ)
               .university("Université Dijon")
               .city("Dijon")
               .build();

       testCenterRepository.save(centreGrenoble);
       testCenterRepository.save(centreDijon);

    CandidateEntity candidate1 = CandidateEntity
            .builder()
            .firstname("Blaste")
            .lastname("Mulamba")
            .email("sudo.blaste@gmail.com")
            .phoneNumber("20641923")
            .birthDate(LocalDate.of(2003, 7, 4))
            .hasExtraTime(false)
            .testCenterEntity(centreDijon)
            .build();

        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .firstname("Félix")
                .lastname("Tshilomba")
                .email("felix.tshilombo@president.com")
                .phoneNumber("52064923")
                .birthDate(LocalDate.of(1998, 3, 21))
                .hasExtraTime(true)
                .testCenterEntity(centreGrenoble)
                .build();

        CandidateEntity candidate3 = CandidateEntity
                .builder()
                .firstname("Basma")
                .lastname("Morabit")
                .email("basma.morabit@univ-grenoble-alpes.fr")
                .phoneNumber("43784923")
                .birthDate(LocalDate.of(2003, 3, 21))
                .hasExtraTime(true)
                .testCenterEntity(centreGrenoble)
                .build();

        CandidateEntity candidate4 = CandidateEntity
                .builder()
                .firstname("Jessica")
                .lastname("Matamoros")
                .email("jessicamatam@yahoo.com")
                .phoneNumber("14567324")
                .birthDate(LocalDate.of(2006, 6, 18))
                .hasExtraTime(false)
                .testCenterEntity(centreGrenoble)
                .build();

    candidateRepository.save(candidate1);
    candidateRepository.save(candidate2);
    candidateRepository.save(candidate3);
    candidateRepository.save(candidate4);

    centreGrenoble.setCandidateEntities(Set.of(candidate2, candidate3, candidate4));
    centreDijon.setCandidateEntities(Set.of(candidate1));

    // when
    Set<CandidateEntity> candidatesAtGre = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE);
    Set<CandidateEntity> candidatesAtDij = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.DIJ);

    // then
    assertThat(candidatesAtGre).hasSize(3);
    assertThat(candidatesAtDij).hasSize(1);

    }

    @Test
    void testFindAllByCandidateEvaluationGridEntitiesGradeLessThan(){
        // given
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .firstname("Blaste")
                .lastname("Mulamba")
                .email("sudo.blaste@gmail.com")
                .phoneNumber("20641923")
                .birthDate(LocalDate.of(2003, 7, 4))
                .hasExtraTime(false)
                .build();

        CandidateEntity candidate2 = CandidateEntity
                .builder()
                .firstname("Félix")
                .lastname("Tshilombo")
                .email("felix.tshilombo@president.com")
                .phoneNumber("52064923")
                .birthDate(LocalDate.of(1998, 3, 21))
                .hasExtraTime(true)
                .build();

        candidateRepository.save(candidate1);
        candidateRepository.save(candidate2);

        CandidateEvaluationGridEntity evalGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(14)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 12, 18, 17, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(4)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 11, 13, 15, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(7)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 11, 13, 15, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid4 = CandidateEvaluationGridEntity
                .builder()
                .grade(16)
                .candidateEntity(candidate2)
                .submissionDate(LocalDateTime.of(2023, 11, 13, 15, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid5 = CandidateEvaluationGridEntity
                .builder()
                .grade(12)
                .candidateEntity(candidate2)
                .submissionDate(LocalDateTime.of(2023, 11, 13, 15, 40))
                .build();

        evaluationGridRepository.save(evalGrid1);
        evaluationGridRepository.save(evalGrid2);
        evaluationGridRepository.save(evalGrid3);
        evaluationGridRepository.save(evalGrid4);
        evaluationGridRepository.save(evalGrid5);

        candidate1.setCandidateEvaluationGridEntities(Set.of(evalGrid1, evalGrid2, evalGrid3));
        candidate2.setCandidateEvaluationGridEntities(Set.of(evalGrid4, evalGrid5));

        // when

        Set<CandidateEntity> candidatesWithGradeLessThan10 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(10);
        Set<CandidateEntity> candidatesWithGradeLessThan3 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(3);

        // then

        assertThat(candidatesWithGradeLessThan10).hasSize(1);
        assertThat(candidatesWithGradeLessThan3).hasSize(0);
    }

    @Test
    void testFindAllByHasExtraTimeFalseAndBirthDateBefore(){

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Hamza")
                .lastname("Cherkaoui")
                .email("cherkaha@gmail.com")
                .phoneNumber("07694945")
                .birthDate(LocalDate.of(2002,6,4))
                .hasExtraTime(false)
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity
                .builder()
                .firstname("Blaste")
                .lastname("Mulamba")
                .email("mulambab@gmail.com")
                .phoneNumber("07329172")
                .birthDate(LocalDate.of(2002,7,4))
                .hasExtraTime(false)
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity
                .builder()
                .firstname("Josman")
                .lastname("Goal")
                .email("Jos@gmail.com")
                .phoneNumber("07901579")
                .birthDate(LocalDate.of(2004,12,22))
                .hasExtraTime(true)
                .build();

        // commentaire

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);

        Set<CandidateEntity> candidatsfound = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2003,12,3));

        assertThat(candidatsfound).hasSize(2);

    }


}
