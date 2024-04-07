package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateEvaluationGridRepository;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import fr.uga.l3miage.spring.tp3.repositories.TestCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CandidateComponent {
    private final CandidateEvaluationGridRepository candidateEvaluationGridRepository;
    private final CandidateRepository candidateRepository;

    // Supposons l'existence d'un repository pour les centres de test
    private final TestCenterRepository testCenterRepository;

    public Set<CandidateEntity> getAllEliminatedCandidate(){
        return candidateEvaluationGridRepository.findAllByGradeIsLessThanEqual(5)
                .stream()
                .map(CandidateEvaluationGridEntity::getCandidateEntity)
                .collect(Collectors.toSet());
    }


    public CandidateEntity getCandidatById(Long id) throws CandidateNotFoundException {
       return candidateRepository.findById(id).orElseThrow(()-> new CandidateNotFoundException(String.format("Le candidat [%s] n'a pas été trouvé",id),id));
    }

    public List<CandidateEntity> getCandidatsByIds(List<Long> listIds) throws CandidateNotFoundException {
        List<CandidateEntity> foundCandidates = candidateRepository.findAllById(listIds);

        Set<Long> foundIds = foundCandidates.stream()
                .map(CandidateEntity::getId)
                .collect(Collectors.toSet());

        listIds.removeAll(foundIds);

        if (!listIds.isEmpty()) {
            // on affiche le premier id non trouvé
            Long missingId = listIds.iterator().next();
            throw new CandidateNotFoundException("Le candidat avec l'ID " + missingId + " existe pas", missingId);
        }
        return foundCandidates;
    }




}
