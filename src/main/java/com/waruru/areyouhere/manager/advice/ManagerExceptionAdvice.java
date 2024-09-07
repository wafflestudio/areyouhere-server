package com.waruru.areyouhere.manager.advice;


import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;

import com.waruru.areyouhere.common.annotation.SlackNotification;
import com.waruru.areyouhere.common.error.ErrorResponse;
import com.waruru.areyouhere.common.utils.Ordered;
import com.waruru.areyouhere.email.exception.EmailSendException;
import com.waruru.areyouhere.email.exception.InvalidEmailDestinationException;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.manager")
@Order(Ordered.SECOND_VALUE)
@Slf4j
public class ManagerExceptionAdvice {

    @ExceptionHandler(UnAuthenticatedException.class)
    public ResponseEntity<HttpStatus> unAuthorizedAccessHandler() {
        return RESPONSE_FORBIDDEN;
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<HttpStatus> duplicatedEmailHandler(){
        return RESPONSE_CONFLICT;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpStatus> invalidInputHandler(){
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(EmailSendException.class)
    @SlackNotification
    public ErrorResponse emailSendException(HttpServletRequest request, Exception e){
        log.error("Internal Server Error", e);
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod());
        sb.append(request.getRemoteAddr());
        sb.append(request.getRequestURL());
        sb.append(e.getMessage());

        return ErrorResponse.of("INTERNAL SERVER ERROR", sb.toString());
    }


    @ExceptionHandler(InvalidEmailDestinationException.class)
    public ResponseEntity<HttpStatus> invalidEmailDestinationHandler(){
        return RESPONSE_BAD_REQUEST;
    }
}
