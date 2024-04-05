package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")

public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    // ajoutez repo testCenter
    @Autowired
    private TestCenterRepository testCenterRepository;

    @Test
    void testRequestFindAllByTestCenterEntityCode(){

        // Given
        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .id(1L)
                .code(TestCenterCode.GRE)
                .university("UGA")
                .city("SMH")
                .build();

        TestCenterEntity testCenterEntity1 = TestCenterEntity
                .builder()
                .id(2L)
                .code(TestCenterCode.DIJ)
                .university("UGA")
                .city("SMH")
                .build();

        testCenterRepository.save(testCenterEntity);
        testCenterRepository.save(testCenterEntity1);

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Jacques")
                .lastname("Bruno")
                .email("jacques@gmail.com")
                .phoneNumber("06789009")
                .testCenterEntity(testCenterEntity)
                .id(1L)
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Rob")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .id(2L)
                .testCenterEntity(testCenterEntity1)
                .build();


        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity1);

        // when
        Set<CandidateEntity> candidatesEntitiesResponses = candidateRepository.findAllByTestCenterEntityCode(TestCenterCode.GRE);

        //then
        assertThat(candidatesEntitiesResponses).hasSize(1);
        assertThat(candidatesEntitiesResponses.stream().findFirst().get().getTestCenterEntity().getCode()).isEqualTo(TestCenterCode.GRE);



    }
}
