package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;
import fr.uga.l3miage.spring.tp3.exceptions.handlers.CreationSessionExceptionHandler;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.CandidateEvaluationResponse;
import fr.uga.l3miage.spring.tp3.responses.EcosSessionProgrammationResponse;
import fr.uga.l3miage.spring.tp3.responses.EcosSessionProgrammationStepResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @Autowired
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @Autowired
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    @SpyBean
    private SessionComponent sessionComponent;

    @AfterEach
    public void clear() {
        ecosSessionRepository.deleteAll();
        ecosSessionProgrammationRepository.deleteAll();
        ecosSessionProgrammationStepRepository.deleteAll();
    }

    @Test
    void canCreateSession(){
        //given
        final HttpHeaders headers = new HttpHeaders();

        final SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .code("101")
                .description("Yo mec")
                .build();

        final SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .label("Label")
                .steps(Set.of(sessionProgrammationStepCreationRequest))
                .build();

        final SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("My Session")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();

        // when
        ResponseEntity<SessionResponse> response = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(sessionCreationRequest, headers), SessionResponse.class);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(ecosSessionRepository.count()).isEqualTo(1);
        assertThat(ecosSessionProgrammationRepository.count()).isEqualTo(1);
        assertThat(ecosSessionProgrammationStepRepository.count()).isEqualTo(1);
        verify(sessionComponent, times(1)).createSession(any(EcosSessionEntity.class));
    }


    // Pas compris mais faut-peut etre le revoir
    @Test
    void canCreateSessionFailed(){

        //Given
        final HttpHeaders headers = new HttpHeaders();
        final Map<String, Object> urlParams = new HashMap<>();

        urlParams.put("idSession", 9999L);

        //when
        ResponseEntity<ExamNotFoundException> response = testRestTemplate.exchange("/api/session/create", HttpMethod.GET, new HttpEntity<>(null, headers), ExamNotFoundException.class, urlParams);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

    @Test
    void changeStatus_Success() {
        // Given
        final HttpHeaders headers = new HttpHeaders();

        // When
        final Map<String, Object> urlParams = new HashMap<>();
        urlParams.put("idSession", 1L);
        ResponseEntity<List<CandidateEvaluationResponse>> response = testRestTemplate.exchange(
                "/api/session/{idSession}/changeStatus",
                HttpMethod.PUT,
                new HttpEntity<>(null, headers),
                new ParameterizedTypeReference<List<CandidateEvaluationResponse>>() {},
                urlParams
        );

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        // Add more assertions based on the expected behavior
    }


}



