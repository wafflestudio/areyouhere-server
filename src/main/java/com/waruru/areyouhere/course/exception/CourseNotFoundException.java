package com.waruru.areyouhere.course.exception;

public class CourseNotFoundException extends CourseException {
    public CourseNotFoundException(String message) {
        super(message);
    }

    public CourseNotFoundException() {
    }

    public CourseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}