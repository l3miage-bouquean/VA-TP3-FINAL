package fr.uga.l3miage.spring.tp3.exceptions.technical;

import lombok.Getter;

@Getter
public class TestCenterNotFoundException extends Exception {
    private final Long testCenterId;

    public TestCenterNotFoundException(String message, Long testCenterId) {
        super(message);
        this.testCenterId = testCenterId;
    }
}