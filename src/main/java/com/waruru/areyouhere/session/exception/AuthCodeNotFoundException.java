package com.waruru.areyouhere.session.exception;

public class AuthCodeNotFoundException extends RuntimeException{
    public AuthCodeNotFoundException() {
    }

    public AuthCodeNotFoundException(String message) {
        super(message);
    }

    public AuthCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
