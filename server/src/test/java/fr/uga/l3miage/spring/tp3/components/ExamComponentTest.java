package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.models.SkillEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {

    @Autowired
    private ExamComponent examComponent;

    @MockBean
    private ExamRepository examRepository;

    @Test
    void testGetAllByIdNotFound() throws ExamNotFoundException{
        // given
        when(examRepository.findById(anyLong())).thenReturn(Optional.empty());
        //then - when
        assertThrows(ExamNotFoundException.class, ()->examComponent.getAllById(Set.of(1L)));

    }

    @Test
    void testGetAllByIdFound(){
        ExamEntity examEntity = ExamEntity.builder()
                .id(1L)
                .name("exam test")
                .startDate(LocalDateTime.of(2024,Month.APRIL,6,14,51))
                .endDate(LocalDateTime.of(2024,Month.APRIL,6,14,52))
                .build();
        // given
        when(examRepository.findById(anyLong())).thenReturn(Optional.of(examEntity));
        //when - then
        assertDoesNotThrow(()->examComponent.getAllById(Set.of()));
    }

    @Test
    void testGetAllCardioExam() {
        // Given
        SkillEntity skillEntity = SkillEntity.builder().name("cardio").build();
        ExamEntity exam1 = ExamEntity.builder().name("NSI").skillEntities(Set.of(skillEntity)).build();
        ExamEntity exam2 = ExamEntity.builder().name("SES").skillEntities(Set.of(skillEntity)).build();

        when(examRepository.findAllBySkillEntitiesContaining(any(SkillEntity.class))).thenReturn(Set.of(exam1, exam2));

        // When - then
        assertDoesNotThrow(() -> examComponent.getAllCardioExam());
    }
}
