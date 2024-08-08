package com.waruru.areyouhere.email.exception;

public class EmailSendException extends RuntimeException{

    public EmailSendException() {
    }

    public EmailSendException(String message) {
        super(message);
    }

    public EmailSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailSendException(Throwable cause) {
        super(cause);
    }

    public EmailSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
