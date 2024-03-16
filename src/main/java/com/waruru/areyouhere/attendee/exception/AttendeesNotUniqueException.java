package com.waruru.areyouhere.attendee.exception;

public class AttendeesNotUniqueException extends RuntimeException{
    public AttendeesNotUniqueException() {
    }

    public AttendeesNotUniqueException(String message) {
        super(message);
    }

    public AttendeesNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
