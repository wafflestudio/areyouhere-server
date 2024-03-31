package com.waruru.areyouhere.course.exception;

public abstract class CourseException extends RuntimeException {
    public CourseException(String message) {
        super(message);
    }

    public CourseException() {
    }

    public CourseException(String message, Throwable cause) {
        super(message, cause);
    }
}
