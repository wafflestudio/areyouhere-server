package com.waruru.areyouhere.attendee.exception;

public class SessionAttendeesEmptyException extends RuntimeException{
    public SessionAttendeesEmptyException() {
    }

    public SessionAttendeesEmptyException(String message) {
        super(message);
    }

    public SessionAttendeesEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
