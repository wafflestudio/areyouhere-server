package com.waruru.areyouhere.attendee.exception;

public class AttendeeAlreadyExistsException extends RuntimeException{
    public AttendeeAlreadyExistsException() {
    }

    public AttendeeAlreadyExistsException(String message) {
        super(message);
    }

    public AttendeeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
