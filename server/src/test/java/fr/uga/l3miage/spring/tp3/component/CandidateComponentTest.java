package fr.uga.l3miage.spring.tp3.component;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

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
    void getCandidateByIdNotFound(){
        // given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then - when
        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getCandidatById(1L));
    }

    @Test
    void getCandidateByIdFound(){
        //Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Sett")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();

        // given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));
        //when - then
        assertDoesNotThrow(()->candidateComponent.getCandidatById(1L));
    }

    // Arriver à recuperer tous les candidates ou non

    @Test
    void getAllCandidateEliminatedNotFound(){
        // given List<CandidateEntity> cand = new ArrayList<>();
        when(candidateRepository.findAllById(Set.of(anyLong()))).thenReturn(Collections.emptyList());
        //then - when
        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getAllEliminatedCandidate());
    }

    // Une fois recupèrer prendre tous ceux qui sont éliminé ou non
    @Test
    void getAllCandidateEliminatedFound(){
        //Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Sett")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Sett 1")
                .lastname("Batman 1")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
                .builder()
                .grade(4.0)
                .candidateEntity(candidateEntity)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity1 = CandidateEvaluationGridEntity
                .builder()
                .grade(6.0)
                .candidateEntity(candidateEntity)
                .build();

        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));

        List<CandidateEntity> cand = new ArrayList<>();
        cand.add(candidateEntity);
        cand.add(candidateEntity1);

        // given
        when(candidateRepository.findAllById(Set.of(anyLong()))).thenReturn(cand);
        //when - then
        assertDoesNotThrow(()->candidateComponent.getAllEliminatedCandidate());
    }

}
