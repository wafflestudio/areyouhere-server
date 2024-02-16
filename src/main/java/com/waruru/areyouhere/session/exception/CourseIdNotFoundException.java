package com.waruru.areyouhere.session.exception;

public class CourseIdNotFoundException extends RuntimeException {
    public CourseIdNotFoundException() {
        super();
    }

    public CourseIdNotFoundException(String message) {
        super(message);
    }

    public CourseIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
