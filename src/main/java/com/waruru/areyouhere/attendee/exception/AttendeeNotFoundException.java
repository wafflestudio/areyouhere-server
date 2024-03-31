package com.waruru.areyouhere.attendee.exception;

public class AttendeeNotFoundException extends RuntimeException{
    public AttendeeNotFoundException() {
        super();
    }

    public AttendeeNotFoundException(String message) {
        super(message);
    }

    public AttendeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
