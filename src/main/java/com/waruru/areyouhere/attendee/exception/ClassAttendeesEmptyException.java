package com.waruru.areyouhere.attendee.exception;

public class ClassAttendeesEmptyException extends RuntimeException{
    public ClassAttendeesEmptyException() {
    }

    public ClassAttendeesEmptyException(String message) {
        super(message);
    }

    public ClassAttendeesEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
