package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.controller.SessionController;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

@AutoConfigureTestDatabase
@AutoConfigureWebClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    // On déclare des repositories liés à ce controller
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private EcosSessionRepository sessionRepository;
    @Autowired
    private EcosSessionProgrammationRepository sessionProgrammationRepository;
    @Autowired
    private EcosSessionProgrammationStepRepository sessionProgrammationStepRepository;

    @SpyBean
    private SessionComponent sessionComponent;

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
    void createSessionNotOk(){
        // A faire ...
    }

}
