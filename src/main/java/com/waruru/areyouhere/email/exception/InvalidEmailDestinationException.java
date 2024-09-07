package com.waruru.areyouhere.email.exception;

public class InvalidEmailDestinationException extends RuntimeException{

    public InvalidEmailDestinationException() {
        super();
    }

    public InvalidEmailDestinationException(String message) {
        super(message);
    }

    public InvalidEmailDestinationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEmailDestinationException(Throwable cause) {
        super(cause);
    }

    protected InvalidEmailDestinationException(String message, Throwable cause, boolean enableSuppression,
                                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
