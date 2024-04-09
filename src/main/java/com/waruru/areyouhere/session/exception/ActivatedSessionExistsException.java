package com.waruru.areyouhere.session.exception;

public class ActivatedSessionExistsException extends RuntimeException{
    public ActivatedSessionExistsException() {
    }

    public ActivatedSessionExistsException(String message) {
        super(message);
    }

    public ActivatedSessionExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
