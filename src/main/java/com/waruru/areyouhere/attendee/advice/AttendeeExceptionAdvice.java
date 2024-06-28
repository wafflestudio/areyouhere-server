package com.waruru.areyouhere.attendee.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.attendee.exception.AttendeeAlreadyExistsException;
import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.common.utils.Ordered;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.exception.ActivatedSessionExistsException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.attendee")
@Order(Ordered.SECOND_VALUE)
public class AttendeeExceptionAdvice {

    @ExceptionHandler(ActivatedSessionExistsException.class)
    public ResponseEntity<HttpStatus> activatedSessionExistsHandler() {
        return RESPONSE_CONFLICT;
    }

    @ExceptionHandler(CourseNotFoundException.class)
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

    @ExceptionHandler(AttendeesNotUniqueException.class)
    public ResponseEntity<HttpStatus> attendeesNotUniqueHandler() {
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(AttendeeAlreadyExistsException.class)
    public ResponseEntity<HttpStatus> attendeeAlreadyExistsHandler() {
        return RESPONSE_CONFLICT;
    }

}
