package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.ExamMapper;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.*;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
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

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionComponent sessionComponent;

    @SpyBean
    private SessionMapper sessionMapper;


    @Test
    void testCreateSession()  {
        // given
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .id(1L)
                .code("101")
                .description("Yo mec")
                .build();

        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .id(1L)
                .label("Label")
                .steps(Set.of(sessionProgrammationStepCreationRequest))
                .build();

        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("My Session")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();


        EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
        ecosSessionEntity.setEcosSessionProgrammationEntity(new EcosSessionProgrammationEntity());
        ecosSessionEntity.setId(1L);
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);

        SessionResponse responseExpected = sessionMapper.toResponse(ecosSessionEntity);

        //when
        SessionResponse response = sessionService.createSession(sessionCreationRequest);


        // then
        assertThat(response).usingRecursiveComparison().isEqualTo(responseExpected);
        verify(sessionMapper,times(2)).toEntity(sessionCreationRequest);
        verify(sessionMapper,times(2)).toResponse(same(ecosSessionEntity));
        verify(sessionComponent,times(1)).createSession(any(EcosSessionEntity.class));

    }

    @Test
    void testCreateSessionThrows()  {
        // given
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder()
                .code("101")
                .description("Yo mec")
                .build();

        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder()
                .label("Label")
                .steps(Set.of(sessionProgrammationStepCreationRequest))
                .build();

        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder()
                .name("My Session")
                .examsId(Set.of())
                .ecosSessionProgrammation(sessionProgrammationCreationRequest)
                .build();

        // when & then
        when(sessionComponent.createSession(any(EcosSessionEntity.class)))
                .thenThrow(new CreationSessionRestException("Message d'erreur"));
        assertThrows(CreationSessionRestException.class, () -> {
            sessionService.createSession(sessionCreationRequest);
        });

        verify(sessionMapper,times(1)).toEntity(sessionCreationRequest);
        verify(sessionComponent, times(1)).createSession(any(EcosSessionEntity.class));

    }



}


