package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    SessionService sessionService;

    @MockBean
    ExamComponent examComponent;
    @MockBean
    SessionComponent sessionComponent;

    @SpyBean
    SessionMapper sessionMapper;

    @Test
    void createSessionOk() throws ExamNotFoundException {
        // given
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .description("Etape 1")
                .build();
        EcosSessionProgrammationStepEntity progSessionStep = sessionMapper.toEntity(sessionProgrammationStepCreationRequest);

        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .steps(Set.of(sessionProgrammationStepCreationRequest))
                .build();
        EcosSessionProgrammationEntity progSession = sessionMapper.toEntity(sessionProgrammationCreationRequest);

        SessionCreationRequest creationRequest = SessionCreationRequest
                .builder()
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();
        EcosSessionEntity session = sessionMapper.toEntity(creationRequest);

        progSessionStep.setDescription("Etape 1");

        progSession.setEcosSessionProgrammationStepEntities(Set.of(progSessionStep));

        session.setEcosSessionProgrammationEntity(progSession);

        ExamEntity exam = ExamEntity
                .builder()
                .id(3L)
                .sessionEntity(session)
                .build();

        session.setExamEntities(Set.of(exam));

        when(sessionComponent.createSession(session)).thenReturn(session);

        SessionResponse expectedResponse = sessionMapper.toResponse(session);

        // when
        SessionResponse actualResponse = sessionService.createSession(creationRequest);

        // then
        assertThat(actualResponse).isEqualTo(null);

    }

    @Test
    void createSessionNotOk() throws ExamNotFoundException {
        when(examComponent.getAllById(Set.of(anyLong()))).thenThrow(ExamNotFoundException.class);
        assertThrows(CreationSessionRestException.class, () -> sessionService.createSession(any(SessionCreationRequest.class)));
    }
}
