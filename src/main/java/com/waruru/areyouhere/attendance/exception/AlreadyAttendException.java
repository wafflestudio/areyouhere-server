package com.waruru.areyouhere.attendance.exception;

public class AlreadyAttendException extends RuntimeException{
    public AlreadyAttendException() {
    }

    public AlreadyAttendException(String message) {
        super(message);
    }

    public AlreadyAttendException(String message, Throwable cause) {
        super(message, cause);
    }
}
