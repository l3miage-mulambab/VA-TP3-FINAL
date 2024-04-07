package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {
    @Autowired
    private CandidateService candidateService;
    // Mocking des dépendances du service
    @MockBean
    CandidateComponent candidateComponent;
    // Mapping avec un Spy pour ne pas altérer le comportement de base ??
    // CandidateMapper non fourni ...

    @Test
    void getCandidateAverage() throws CandidateNotFoundException {
        // given

        // Create an exam entity
        ExamEntity exam = ExamEntity
                .builder()
                .id(3L)
                .name("chimie")
                .weight(1) // coeff de l'exam : si 2 alors moyenne = * 2
                .build();

        // Create a candidate entity
        CandidateEntity candidate1 = CandidateEntity
                .builder()
                .id(1L)
                .firstname("Blaste")
                .lastname("Mulamba")
                .hasExtraTime(true)
                .build();

        // Create some grades for the candidate and the exam above
        CandidateEvaluationGridEntity evalGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(14)
                .examEntity(exam)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 12, 18, 17, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(16)
                .examEntity(exam)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 11, 13, 15, 40))
                .build();

        CandidateEvaluationGridEntity evalGrid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(9)
                .examEntity(exam)
                .candidateEntity(candidate1)
                .submissionDate(LocalDateTime.of(2023, 10, 05, 15, 40))
                .build();

        // Make sure of the bidirectional association
        exam.setCandidateEvaluationGridEntities(Set.of(evalGrid1, evalGrid2, evalGrid3));
        candidate1.setCandidateEvaluationGridEntities(Set.of(evalGrid1, evalGrid2, evalGrid3));

        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidate1);

        // when

        double expectedAverage = 13;
        double average = candidateService.getCandidateAverage(1L);

        // then

        assertThat(average).isEqualTo(expectedAverage);
    }

    @Test
    void getCandidateAverageNotFound() throws CandidateNotFoundException {
        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);
        assertThrows(CandidateNotFoundRestException.class, () -> candidateService.getCandidateAverage(1L));
    }

}
