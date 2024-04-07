package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.controller.SessionController;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationGridResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.HttpStatus.*;

@AutoConfigureTestDatabase
@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private SessionService sessionService;

    // On déclare des repositories liés à ce controller
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private EcosSessionRepository sessionRepository;
    @Autowired
    private EcosSessionProgrammationRepository sessionProgrammationRepository;
    @Autowired
    private EcosSessionProgrammationStepRepository sessionProgrammationStepRepository;

    // On souhaite suivre les composants Session et Exam
    @SpyBean
    private SessionComponent sessionComponent;
    @SpyBean
    private ExamComponent examComponent;

    @AfterEach
    void clearRepo(){
        examRepository.deleteAll();
        sessionRepository.deleteAll();
        sessionProgrammationRepository.deleteAll();
        sessionProgrammationStepRepository.deleteAll();
    }

    @Test
    void createSessionOk(){
        // A faire ...
    }

    @Test
    // On tente de créer une session d'examen avec un id d'examen incorrect
    void createSessionNotOk(){
        // given

        final HttpHeaders headers = new HttpHeaders();

        // On fait une requete de création d'étape de session
        SessionProgrammationStepCreationRequest stepRequest = SessionProgrammationStepCreationRequest
                .builder()
                .code("FiRsT")
                .description("Première étape")
                .build();

        // On fait une requete de création de programmation de session avec l'étape ci-haut
        SessionProgrammationCreationRequest programmationRequest = SessionProgrammationCreationRequest
                .builder()
                .steps(Set.of(stepRequest))
                .build();

        /* On fait une requete de création de session programmé comme ci-haut. Dans la requête
        on précise comme id d'examen 1L alors alors que l'examen de id 1L n'existe pas */
        SessionCreationRequest sessionRequest = SessionCreationRequest
                .builder()
                .name("sessionTest")
                .examsId(Set.of(1L))
                .ecosSessionProgrammation(programmationRequest)
                .build();

        // when

        // La réponse retournée lorsqu'on tente de créer la session
        ResponseEntity<String> response = testRestTemplate
                .exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionRequest, headers), String.class);

        // On s'attend à recevoir un status 400 BAD_REQUEST
        assertThat(response.getStatusCodeValue()).isEqualTo(400);

    }

    @Test
    // On veut tester la nouvelle méthode créée dans SessionEndpoints
    void terminateSession(){ // Test non terminé ...
        // given

        // On suppose un examen donné
        ExamEntity exam = ExamEntity
                .builder()
                .name("Chimie")
                .build();

        // Une session de cet examen qui est à l'état EVAL_STARTED
        EcosSessionEntity session = EcosSessionEntity
                .builder()
                .examEntities(Set.of(exam)) // on lie la session à l'examen
                .status(SessionStatus.EVAL_STARTED)
                .build();

        exam.setSessionEntity(session); // on lie l'examen à la session

        // On crée la dernière étape de la session en supposant
        // qu'elle est terminée il y a 5 heures par exemple
        EcosSessionProgrammationStepEntity lastStep = EcosSessionProgrammationStepEntity
                .builder()
                .code("LaStTeP")
                .dateTime(LocalDateTime.now().minusHours(5))
                .build();

        // On crée quelques notes pour un candidat lors de cet examen
        CandidateEvaluationGridEntity evalGrid1 = CandidateEvaluationGridEntity
                .builder()
                .grade(17)
                .build();

        CandidateEvaluationGridEntity evalGrid2 = CandidateEvaluationGridEntity
                .builder()
                .grade(13)
                .build();

        CandidateEvaluationGridEntity evalGrid3 = CandidateEvaluationGridEntity
                .builder()
                .grade(9)
                .build();

        // On lie les notes créées à cet examen
        exam.setCandidateEvaluationGridEntities(Set.of(evalGrid1, evalGrid2, evalGrid3));

        // On lance la création de la session au sein du composant
        sessionComponent.createSession(session);

        final HttpHeaders headers = new HttpHeaders();

        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("sessionId", 1);

        // when -- TODO

        /*   On veut faire passer le session status à EVAL_ENDED   */

        // then -- TODO
    }
}
