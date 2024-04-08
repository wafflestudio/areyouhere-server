package com.waruru.areyouhere.attendance.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.attendance.exception.AlreadyAttendException;
import com.waruru.areyouhere.attendance.exception.DuplicateAuthCodeAttendException;
import com.waruru.areyouhere.attendance.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.attendance")
public class AttendanceExceptionAdvice {

    @ExceptionHandler(SessionIdNotFoundException.class)
    public ResponseEntity<HttpStatus> sessionIdNotFoundHandler() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity<HttpStatus> studentNameNotFoundHandler() {
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(AuthCodeNotFoundException.class)
    public ResponseEntity<HttpStatus> authCodeNotFoundHandler() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(AlreadyAttendException.class)
    public ResponseEntity<HttpStatus> alreadyAttendHandler() {
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(DuplicateAuthCodeAttendException.class)
    public ResponseEntity<HttpStatus> duplicateAuthCodeAttendHandler() {
        return RESPONSE_FORBIDDEN;
    }
}
