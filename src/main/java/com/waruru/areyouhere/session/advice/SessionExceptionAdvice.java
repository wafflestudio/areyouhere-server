package com.waruru.areyouhere.session.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
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
}
