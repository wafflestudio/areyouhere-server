package com.waruru.areyouhere.user.advice;


import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;

import com.waruru.areyouhere.user.exception.DuplicatedEmailException;
import com.waruru.areyouhere.user.exception.MemberNotFoundException;
import com.waruru.areyouhere.user.exception.UnAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.user")
public class UserExceptionAdvice {
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<HttpStatus> memberNotFoundException() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> unAuthorizedAccessException() {
        return RESPONSE_FORBIDDEN;
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<HttpStatus> duplicatedEmailException(){
        return RESPONSE_CONFLICT;
    }

}
