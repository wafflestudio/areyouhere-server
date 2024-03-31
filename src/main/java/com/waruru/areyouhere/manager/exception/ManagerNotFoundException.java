package com.waruru.areyouhere.manager.exception;

import com.waruru.areyouhere.course.exception.CourseException;

public class ManagerNotFoundException extends CourseException {
    public ManagerNotFoundException(String message) {
        super(message);
    }

}
