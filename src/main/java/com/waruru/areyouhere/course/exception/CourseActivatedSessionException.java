package com.waruru.areyouhere.course.exception;

public class CourseActivatedSessionException extends RuntimeException{
    public CourseActivatedSessionException() {
        super();
    }

    public CourseActivatedSessionException(String message) {
        super(message);
    }

    public CourseActivatedSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
