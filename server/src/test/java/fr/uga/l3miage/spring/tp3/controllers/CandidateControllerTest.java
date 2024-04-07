package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @AfterEach
    public void clear(){
        candidateRepository.deleteAll();
    }

    @Test
    void GetCandidateAverageDontThrow(){

        final HttpHeaders headers = new HttpHeaders();

        final HashMap<String,Long> urlParam = new HashMap<>();

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("candidat@gmail.com")
                .id(1L)
                .build();

        ExamEntity examEntity = ExamEntity
                .builder()
                .weight(3)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
                .builder()
                .grade(13)
                .build();

        candidateEvaluationGridEntity.setExamEntity(examEntity);
        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));
        candidateRepository.save(candidateEntity);

        urlParam.put("idCandidate", 1L);
       //erreur avec Double dans le exchange
        ResponseEntity<Double> response = testRestTemplate.exchange("/api/candidates/{idCandidate}/average", HttpMethod.GET, new HttpEntity<>(null, headers), Double.class ,urlParam);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    void getCandidateAverageThrow(){
        final HttpHeaders headers = new HttpHeaders();

        final HashMap<String,Object> urlParam = new HashMap<>();

        urlParam.put("idCandidate", "Le candidat n'a pas été trouvé");

        ResponseEntity<NotFoundException> response = testRestTemplate.exchange("/api/candidates/{idCandidate}", HttpMethod.GET, new HttpEntity<>(null, headers), NotFoundException.class, urlParam);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }


}
