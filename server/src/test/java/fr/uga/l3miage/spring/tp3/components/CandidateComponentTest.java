package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {

    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    @MockBean
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

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

    @Test
    void testGetAllEliminatedCandidate(){
        // Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Sett")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();
        //Given
        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Sett1")
                .lastname("Batman1")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();


        CandidateEvaluationGridEntity candidateEvaluationGrid = CandidateEvaluationGridEntity.builder().grade(19).candidateEntity(candidateEntity).build();
        CandidateEvaluationGridEntity candidateEvaluationGrid1 = CandidateEvaluationGridEntity.builder().grade(4).candidateEntity(candidateEntity1).build();

        candidateEvaluationGridRepository.save(candidateEvaluationGrid);
        candidateEvaluationGridRepository.save(candidateEvaluationGrid1);

        when(candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5))
                .thenReturn(Stream.of(candidateEvaluationGrid,candidateEvaluationGrid1)
                        .collect(Collectors.toSet()));

        // When
        Set<CandidateEntity> eliminatedCandidates = candidateComponent.getAllEliminatedCandidate();

        // Then
        assertEquals(2, eliminatedCandidates.size());
        assertTrue(eliminatedCandidates.contains(candidate1));
        assertTrue(eliminatedCandidates.contains(candidate2));
        assertFalse(eliminatedCandidates.contains(candidate3));

    }



}
