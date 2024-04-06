package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.models.SkillEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class ExamRepositoryTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private EcosSessionRepository ecosSessionRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Test
    void testFindAllBySkillEntitiesContaining(){

        // Given
        SkillEntity skillEntity = SkillEntity
                .builder()
                .name("My Skill")
                .build();

        SkillEntity skillEntity1 = SkillEntity
                .builder()
                .name("My Skill1")
                .build();

        skillRepository.save(skillEntity);
        skillRepository.save(skillEntity1);

        ExamEntity examEntitySES = ExamEntity
                .builder()
                .name("SES")
                .weight(1)
                .build();

        ExamEntity examEntityNSI= ExamEntity
                .builder()
                .name("NSI")
                .weight(1)
                .skillEntities(Set.of(skillEntity))
                .build();

        examRepository.save(examEntityNSI);


        // When
        Set<ExamEntity> examEntities = examRepository.findAllBySkillEntitiesContaining(skillEntity);


        // Then
        assertThat(examEntities).hasSize(1);

    }





}
