package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;

import fr.uga.l3miage.spring.tp3.request.TestCenterAddStudentsRequest;
import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;



@AutoConfigureWebTestClient
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class TestCenterController {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @SpyBean
    private TestCenterComponent testCenterComponent;

    @SpyBean
    private CandidateComponent candidateComponent;

    @AfterEach
    public void clear() {
        candidateRepository.deleteAll();
        testCenterRepository.deleteAll();
    }


    @Test
    void testAddStudentColletionToTestCenter() throws TestCenterNotFoundException, CandidateNotFoundException {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        final HttpHeaders headers = new HttpHeaders();

        CandidateEntity candidateEntity1 = CandidateEntity.builder()
                .email("test01@test.com")
                .birthDate(LocalDate.of(2012, 3, 5))
                .build();

        CandidateEntity candidateEntity2 = CandidateEntity.builder()
                .email("test02@test.com")
                .birthDate(LocalDate.of(2001, 3, 5))
                .build();

        CandidateEntity candidateEntity3 = CandidateEntity.builder()
                .email("test03@test.com")
                .birthDate(LocalDate.of(1999, 3, 5))
                .build();

        TestCenterEntity testCenterEntity = TestCenterEntity.builder()
                .university("Test UGA")
                .code(TestCenterCode.GRE)
                .candidateEntities(Set.of())
                .build();


        candidateRepository.save(candidateEntity1);
        candidateRepository.save(candidateEntity2);
        candidateRepository.save(candidateEntity3);
        testCenterRepository.save(testCenterEntity);

        TestCenterRequest request = TestCenterRequest.builder()
                .testCenterId(4L)
                .studentIds(Set.of(1L, 2L, 3L))
                .build();

        //When
        ResponseEntity<Boolean> response = testRestTemplate.exchange("/api/testCenter/students/update", HttpMethod.PATCH, new HttpEntity<TestCenterRequest>(request, headers), Boolean.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        assertTrue(response.getBody());
        verify(testCenterComponent, times(1)).addStudentsToTestCenter(any(TestCenterEntity.class), anySet());
        verify(testCenterComponent, times(1)).getTestCenterById(any());
        verify(candidateComponent, times(1)).getCandidatsByIds(any());

    }
}