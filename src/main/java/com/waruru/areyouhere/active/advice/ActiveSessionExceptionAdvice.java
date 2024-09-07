package com.waruru.areyouhere.active.advice;


import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;

import com.waruru.areyouhere.attendance.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.common.utils.Ordered;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.waruru.areyouhere.active")
@Order(Ordered.SECOND_VALUE)
public class ActiveSessionExceptionAdvice {
    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<HttpStatus> courseNotFoundFoundHandler() {
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(SessionIdNotFoundException.class)
    public ResponseEntity<HttpStatus> sessionIdNotFoundHandler(){
        return RESPONSE_NOT_FOUND;
    }

    @ExceptionHandler(AuthCodeNotFoundException.class)
    public ResponseEntity<HttpStatus> authCodeNotFoundHandler(){ return RESPONSE_NOT_FOUND; }


}
