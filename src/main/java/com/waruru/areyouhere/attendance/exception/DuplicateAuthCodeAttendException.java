package com.waruru.areyouhere.attendance.exception;

public class DuplicateAuthCodeAttendException extends RuntimeException{
    public DuplicateAuthCodeAttendException() {
    }

    public DuplicateAuthCodeAttendException(String message) {
        super(message);
    }

    public DuplicateAuthCodeAttendException(String message, Throwable cause) {
        super(message, cause);
    }
}
