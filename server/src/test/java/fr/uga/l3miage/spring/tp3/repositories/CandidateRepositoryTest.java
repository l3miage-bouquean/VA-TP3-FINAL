package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")

public class CandidateRepositoryTest {

    // En gros dans les entities :     @GeneratedValue(strategy = GenerationType.AUTO) = Auto incrementation des ID donc en gros pas bsn de declarer l'id


    @Autowired
    private CandidateRepository candidateRepository;
    // ajoutez repo testCenter
    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Test
    void testRequestFindAllByTestCenterEntityCode(){

        // Given
        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .code(TestCenterCode.GRE)
                .university("UGA")
                .city("SMH")
                .build();

        TestCenterEntity testCenterEntity1 = TestCenterEntity
                .builder()
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
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Rob")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
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

    @Test
    void testRequestfindAllByCandidateEvaluationGridEntitiesGradeLessThan(){

        // Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Jacques")
                .lastname("Bruno")
                .email("jacques@gmail.com")
                .phoneNumber("06789009")
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Rob")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .build();

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity1);

        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
                .builder()
                .grade(5.0)
                .candidateEntity(candidateEntity)
                .build();

        CandidateEvaluationGridEntity candidateEvaluationGridEntity1 = CandidateEvaluationGridEntity
                .builder()
                .grade(5.0)
                .candidateEntity(candidateEntity1)
                .build();

        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity);
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity1);

        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));
        candidateEntity1.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity1));

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity1);

        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(6.0);
        //Set<CandidateEntity> candidateEntitiesResponses1 = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(16.0);

        //then
        assertThat(candidateEntitiesResponses).hasSize(2);

        // A revoir
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getCandidateEvaluationGridEntities());

    }


    @Test
    void testRequestFindAllByHasExtraTimeFalseAndBirthDateBefore(){
        // Given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("Jacques")
                .lastname("Bruno")
                .email("jacques@gmail.com")
                .hasExtraTime(false)
                .birthDate(LocalDate.of(2003,10,26))
                .phoneNumber("06789009")
                .build();

        CandidateEntity candidateEntity1 = CandidateEntity
                .builder()
                .firstname("Rob")
                .lastname("Batman")
                .email("jm@gmail.com")
                .phoneNumber("09090909")
                .hasExtraTime(true)
                .birthDate(LocalDate.of(2009,12,26))
                .build();

        candidateRepository.save(candidateEntity);
        candidateRepository.save(candidateEntity1);

        // when
        Set<CandidateEntity> candidateEntitiesResponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2010, 1,1));

        // then
        assertThat(candidateEntitiesResponses).hasSize(1);
        assertThat(candidateEntitiesResponses.stream().findFirst().get().getBirthDate()).isBefore(LocalDate.of(2010,1,1));
    }
}
