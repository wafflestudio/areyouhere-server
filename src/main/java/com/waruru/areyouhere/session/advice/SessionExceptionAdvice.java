package com.waruru.areyouhere.session.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.exception.ActivatedSessionExistsException;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.session")
public class SessionExceptionAdvice {

    @ExceptionHandler(CurrentSessionNotFoundException.class)
    public ResponseEntity<HttpStatus> currentSessionNotFoundHandler(){
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(SessionIdNotFoundException.class)
    public ResponseEntity<HttpStatus> sessionIdNotFoundHandler(){
        return RESPONSE_NOT_FOUND;
    }


    @ExceptionHandler(CurrentSessionDeactivatedException.class)
    public ResponseEntity<HttpStatus> currentSessionDeactivatedHandler(){
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<HttpStatus> courseNotFoundFoundHandler() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatus> methodArgumentNotValidHandler(){
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(AttendeeNotFoundException.class)
    public ResponseEntity<HttpStatus> attendeeNotFoundHandler(){
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(ActivatedSessionExistsException.class)
    public ResponseEntity<HttpStatus> activatedSessionExistsHandler(){
        return RESPONSE_CONFLICT;
    }
}
