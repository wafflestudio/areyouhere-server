package com.waruru.areyouhere.attendance.exception;

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
