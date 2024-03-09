package com.waruru.areyouhere.session.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NO_CONTENT;

import com.waruru.areyouhere.session.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.exception.StudentNameNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

    @ExceptionHandler(CourseIdNotFoundException.class)
    public ResponseEntity<HttpStatus> courseIdNotFoundFoundHandler() {
        return RESPONSE_NO_CONTENT;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatus> methodArgumentNotValidHandler(){
        return RESPONSE_BAD_REQUEST;
    }
}
