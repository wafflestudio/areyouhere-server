package com.waruru.areyouhere.session.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.session.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.exception.StudentNameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(AuthCodeNotFoundException.class)
    public ResponseEntity<HttpStatus> authCodeNotFoundHandler(){
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(StudentNameNotFoundException.class)
    public ResponseEntity<HttpStatus> studentNameNotFoundHandler() {
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(CurrentSessionDeactivatedException.class)
    public ResponseEntity<HttpStatus> currentSessionDeactivatedHandler(){
        return RESPONSE_NO_CONTENT;
    }
}
