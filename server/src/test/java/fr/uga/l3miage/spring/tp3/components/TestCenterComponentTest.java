package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;


import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TestCenterComponentTest {
    @Autowired
    private TestCenterComponent testCenterComponent;

    @MockBean
    private TestCenterRepository testCenterRepository;

    @MockBean
    private CandidateRepository candidateRepository;

    @Test
    void testFoundGetTestCenterById() {
        // Given
        TestCenterEntity testCenterEntity = TestCenterEntity.builder()
                .university("")
                .build();

        when(testCenterRepository.findById(anyLong())).thenReturn(Optional.of(testCenterEntity));

        // When - Then
        assertDoesNotThrow(() -> testCenterComponent.getTestCenterById(1L));
    }

    @Test
    void testNotFoundGetTestCenterById() {
        // Given
        when(testCenterRepository.findById(anyLong())).thenReturn(Optional.empty());
        // When - Then
        assertThrows(TestCenterNotFoundException.class, () -> testCenterComponent.getTestCenterById(1L));
    }

    @Test
    void testAddStudentToTestCenter() throws TestCenterNotFoundException {
        // Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Sett")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .birthDate(LocalDate.of(2003, 10, 26))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity.builder()
                .email("jm1@gmail.com")
                .birthDate(LocalDate.of(2011, 2, 1))
                .build();

        TestCenterEntity testCenterEntity = TestCenterEntity.builder()
                .university("LoL")
                .id(100L)
                .candidateEntities(Set.of())
                .build();

        when(candidateRepository.save(any(CandidateEntity.class))).thenReturn(candidateEntity);


        // When
        boolean result = testCenterComponent.addStudentsToTestCenter(testCenterEntity, Set.of(candidateEntity, candidateEntity2));
        // Then
        assertTrue(result);

    }

    }
