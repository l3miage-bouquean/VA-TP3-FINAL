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

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {

    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;

    @Test
    void testCandidateAverageFound() throws CandidateNotFoundException {
        // given
        // Une note = un exam
        ExamEntity examEntitySES = ExamEntity
                .builder()
                .id(1L)
                .name("SES")
                .weight(1)
                .build();

        ExamEntity examEntityNSI= ExamEntity
                .builder()
                .id(1L)
                .name("NSI")
                .weight(1)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntitySES = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .examEntity(examEntitySES)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntityNSI = CandidateEvaluationGridEntity
                .builder()
                .grade(20)
                .examEntity(examEntityNSI)
                .build();

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id(1L)
                .firstname("Sett")
                .lastname("Batman")
                .email("bat@gmail.com")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntityNSI,candidateEvaluationGridEntitySES))
                .build();

        //when
        when(candidateComponent.getCandidatById(anyLong())).thenReturn((candidateEntity));
        Double response = candidateService.getCandidateAverage(1L);

        // then
        assertThat(response).isEqualTo(17.5);
        verify(candidateComponent,times(1)).getCandidatById(any(Long.class));
    }

    @Test
    void testCandidateAverageNotFound() throws CandidateNotFoundException {
        // given
        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);
        assertThrows(CandidateNotFoundRestException.class, () -> candidateService.getCandidateAverage(1L));
    }

}
