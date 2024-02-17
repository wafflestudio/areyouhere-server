package com.waruru.areyouhere.session.exception;

public class StudentNameNotFoundException extends RuntimeException{
    public StudentNameNotFoundException() {
        super();
    }

    public StudentNameNotFoundException(String message) {
        super(message);
    }

    public StudentNameNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
