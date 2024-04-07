package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {
    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @Test
    void testGetCandidatNotFound(){
        // when
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(CandidateNotFoundException.class,()->candidateComponent.getCandidatById(9L));

    }

    @Test
    void testGetCandidatFound(){
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id(1L) // NOT SURE
                .hasExtraTime(false)
                .birthDate(LocalDate.of(2002,6,4))
                .build();
        //when
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        assertDoesNotThrow(()->candidateRepository.findById(1L));

    }

}
