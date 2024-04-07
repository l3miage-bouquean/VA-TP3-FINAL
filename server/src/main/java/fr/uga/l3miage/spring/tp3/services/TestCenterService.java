package fr.uga.l3miage.spring.tp3.services;


import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.components.TestCenterComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.TestCenterRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.TestCenterNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TestCenterService {
    private final CandidateComponent candidateComponent;
    private final TestCenterComponent testCenterComponent;

    public boolean addStudentsToTestCenter(TestCenterRequest testCenterUpdateRequest) {
        try {
            TestCenterEntity testCenterEntity = testCenterComponent.getTestCenterById(testCenterUpdateRequest.getTestCenterId());
            Set<CandidateEntity> candidateEntityList = new HashSet<>(candidateComponent.getCandidatsByIds((List<Long>) testCenterUpdateRequest.getStudentIds())) ;
            return testCenterComponent.addStudentsToTestCenter(testCenterEntity, candidateEntityList);
        }
        catch (TestCenterNotFoundException | CandidateNotFoundException e) {
            throw new TestCenterRestException(e.getMessage(), ((TestCenterNotFoundException) e).getTestCenterId(), null);
        }

    }


}
