package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.AfterEach;
import org.springframework.http.*;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")

public class CandidateControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @SpyBean
    private CandidateComponent candidateComponent;


    @AfterEach
    public void clear() {
        candidateRepository.deleteAll();
    }

    @Test
    void testGetCandidateAverageSucess() throws CandidateNotFoundException {
        //given
        final HttpHeaders headers = new HttpHeaders();

        // Creation of CandidateEntity, CandidateEvaluationGrid and ExamEntity (not necessary)
        ExamEntity examEntitySES = ExamEntity
                .builder()
                .name("SES")
                .weight(1)
                .build();

        ExamEntity examEntityNSI= ExamEntity
                .builder()
                .name("NSI")
                .weight(3)
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
                .firstname("Sett")
                .lastname("Batman")
                .email("bat@gmail.com")
                .candidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntityNSI,candidateEvaluationGridEntitySES))
                .build();

        candidateRepository.save(candidateEntity);
        Long candidateId = 1L; // Obtenez l'ID du candidat enregistré

        // when
        ResponseEntity<Double> response = testRestTemplate.exchange("/api/candidates/{idCandidate}/average", HttpMethod.POST, new HttpEntity<>(null, headers), Double.class, candidateId);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(candidateRepository.count()).isEqualTo(1);
        verify(candidateComponent, times(1)).getCandidatById(any(Long.class));
    }


    @Test
    void testGetCandidateAverageNotFound(){
        //Given
        final HttpHeaders headers = new HttpHeaders();
        final Map<String, Object> urlParams = new HashMap<>();

        urlParams.put("idCandidate", 9999L);

        CandidatNotFoundResponse expectedResponse = CandidatNotFoundResponse
                .builder()
                .candidateId(9999L)
                .uri("/api/candidates/" + 9999L + "/average")
                .errorMessage("Le candidat [9999] n'a pas été trouvé")
                .build();
        //when
        ResponseEntity<CandidatNotFoundResponse> response = testRestTemplate.exchange("/api/candidates/{idCandidate}/average", HttpMethod.GET, new HttpEntity<>(null, headers), CandidatNotFoundResponse.class, urlParams);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).usingRecursiveComparison()
                        .isEqualTo(expectedResponse);

    }

}
