package com.waruru.areyouhere.attendee.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.attendee")
public class AttendeeExceptionAdvice {
    @ExceptionHandler(CourseIdNotFoundException.class)
    public ResponseEntity<HttpStatus> courseIdNotFoundFoundHandler() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(SessionAttendeesEmptyException.class)
    public ResponseEntity<HttpStatus> sessionAttendeesEmptyHandler() {
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(ClassAttendeesEmptyException.class)
    public ResponseEntity<HttpStatus> classAttendeesEmptyHandler() {
        return RESPONSE_NO_CONTENT;
    }

}
