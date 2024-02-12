package com.waruru.areyouhere.user.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(Throwable cause) {
        super(cause);
    }

    protected MemberNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
