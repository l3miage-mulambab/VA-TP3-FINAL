package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    @Test
    void testCreationSession(){

        EcosSessionProgrammationStepEntity sessionProgrammationStep =  EcosSessionProgrammationStepEntity
                .builder()
                .code("3A2")
                .build();

        EcosSessionProgrammationEntity sessionProgramme = EcosSessionProgrammationEntity
                .builder()
                .label("label1")
                .ecosSessionProgrammationStepEntities(Set.of(sessionProgrammationStep))
                .build();


        sessionProgrammationStep.setEcosSessionProgrammationEntity(sessionProgramme);

        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .id(1L)
                .name("session1")
                .status(SessionStatus.CREATED)
                .build();

        ecosSessionEntity.setEcosSessionProgrammationEntity(sessionProgramme);

       when(sessionComponent.createSession(ecosSessionEntity)).thenReturn(ecosSessionEntity);
        // when(ecosSessionRepository.save(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        EcosSessionEntity createdsession = sessionComponent.createSession(ecosSessionEntity);
        assertThat(createdsession).isEqualTo(ecosSessionEntity);



    }



}
