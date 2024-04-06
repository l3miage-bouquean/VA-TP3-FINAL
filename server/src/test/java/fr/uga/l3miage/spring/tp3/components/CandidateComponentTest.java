package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
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



}
