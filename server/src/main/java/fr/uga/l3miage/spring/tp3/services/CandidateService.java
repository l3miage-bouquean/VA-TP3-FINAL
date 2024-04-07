package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateComponent candidateComponent;
    private final TestCenterRepository testCenterRepository; // Exemple hypothétique

    public Double getCandidateAverage(Long candidateId) {
        try {
            CandidateEntity candidateEntity = candidateComponent.getCandidatById(candidateId);
            return (candidateEntity.getCandidateEvaluationGridEntities().stream().reduce(0d, (average, grid) -> average + (grid.getGrade() * grid.getExamEntity().getWeight()), Double::sum))
                    / candidateEntity.getCandidateEvaluationGridEntities().stream().reduce(0,(acc,grid) -> acc + grid.getExamEntity().getWeight(),Integer::sum);
        } catch (CandidateNotFoundException e) {
            throw new CandidateNotFoundRestException(e.getMessage(),e.getCandidateId());
        }
    }

    public Boolean addStudentsToTestCenter(Long centerId, Set<Long> studentIds) {
        boolean allStudentsAdded = true;
        for (Long studentId : studentIds) {
            try {
                CandidateEntity student = candidateComponent.getCandidatById(studentId);
                if (LocalDate.now().getYear() - student.getBirthDate().getYear() < 18) {
                    throw new RuntimeException("L'étudiant avec ID " + studentId + " n'a pas l'âge requis.");
                }
            } catch (CandidateNotFoundException e) {
                allStudentsAdded = false;
            }
        }
        return allStudentsAdded;
    }
}
