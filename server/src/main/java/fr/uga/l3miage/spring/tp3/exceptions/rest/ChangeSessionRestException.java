package fr.uga.l3miage.spring.tp3.exceptions.rest;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;

public class ChangeSessionRestException extends RuntimeException{
    private final String uri;
    private final SessionStatus currentStatus;

    public ChangeSessionRestException(String message, String uri, SessionStatus currentStatus) {
        super(message);
        this.uri = uri;
        this.currentStatus = currentStatus;
    }

}
