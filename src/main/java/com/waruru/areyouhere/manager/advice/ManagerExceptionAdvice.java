package com.waruru.areyouhere.manager.advice;


import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;

import com.waruru.areyouhere.common.utils.Ordered;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.manager")
@Order(Ordered.SECOND_VALUE)
public class ManagerExceptionAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> unAuthorizedAccessHandler() {
        return RESPONSE_FORBIDDEN;
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<HttpStatus> duplicatedEmailHandler(){
        return RESPONSE_CONFLICT;
    }

}
