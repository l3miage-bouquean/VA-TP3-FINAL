package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.CandidatNotFoundResponse;

import fr.uga.l3miage.spring.tp3.exceptions.TestCenterStudentNotFound;
import fr.uga.l3miage.spring.tp3.exceptions.rest.TestCenterRestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class TestCenterExceptionHandler {

    @ExceptionHandler(TestCenterRestException.class)
    public ResponseEntity<TestCenterStudentNotFound> handle(HttpServletRequest httpServletRequest, Exception exception){
        TestCenterRestException updateTestCenterRestException = (TestCenterRestException) exception;
        TestCenterStudentNotFound response = TestCenterStudentNotFound
                .builder()
                .testCenterId(updateTestCenterRestException.getTestCenterId())
                .candidateId(updateTestCenterRestException.getCandidateId())
                .errorMessage(updateTestCenterRestException.getMessage())
                .uri(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(404).body(response);
    }
}