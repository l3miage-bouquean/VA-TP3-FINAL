package fr.uga.l3miage.spring.tp3.exceptions.technical;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;


public class ChangeSessionStateException extends Exception {
    private final String uri;
    private final SessionStatus currentStatus;

    public ChangeSessionStateException(String message, String uri, SessionStatus currentStatus) {
        super(message);
        this.uri = uri;
        this.currentStatus = currentStatus;
    }

}
