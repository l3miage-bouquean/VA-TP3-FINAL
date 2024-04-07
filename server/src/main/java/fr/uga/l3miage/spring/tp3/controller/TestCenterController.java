package fr.uga.l3miage.spring.tp3.controller;
import fr.uga.l3miage.spring.tp3.endpoints.TestCenterEndpoints;
import fr.uga.l3miage.spring.tp3.request.TestCenterRequest;
import fr.uga.l3miage.spring.tp3.services.TestCenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
@Controller
@RequiredArgsConstructor
public class  TestCenterController implements TestCenterEndpoints {
    private final TestCenterService testCenterService;

    @Override
    public boolean addStudentsToTestCenter(TestCenterRequest request) {
        return testCenterService.addStudentsToTestCenter(request);
    }
}