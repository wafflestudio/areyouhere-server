package com.waruru.areyouhere.session.exception;

public class CurrentSessionDeactivatedException extends RuntimeException{
    public CurrentSessionDeactivatedException() {
        super();
    }

    public CurrentSessionDeactivatedException(String message) {
        super(message);
    }

    public CurrentSessionDeactivatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
