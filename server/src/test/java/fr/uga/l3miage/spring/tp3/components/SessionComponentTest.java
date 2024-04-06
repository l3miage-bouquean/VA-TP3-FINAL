package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @Test
    void testCreateSessionNotFound(){
        // given
        when(ecosSessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then - when
        assertThrows(NullPointerException.class, ()->sessionComponent.createSession(new EcosSessionEntity()));
    }

    @Test
    void testCreateSessionFound(){
        //Given

        EcosSessionProgrammationStepEntity ecosSessionProgrammationStepEntity = EcosSessionProgrammationStepEntity
                .builder()
                .code("101")
                .description("Vas-y mec")
                .build();

        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = EcosSessionProgrammationEntity
                .builder()
                .label("Label")
                .ecosSessionProgrammationStepEntities(Set.of(ecosSessionProgrammationStepEntity))
                .build();


        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .name("Session 1")
                .ecosSessionProgrammationEntity(ecosSessionProgrammationEntity)
                .build();

        // given
        when(ecosSessionRepository.findById(anyLong())).thenReturn(Optional.of(ecosSessionEntity));
        //when - then
        assertDoesNotThrow(()->sessionComponent.createSession(ecosSessionEntity));
    }
}
