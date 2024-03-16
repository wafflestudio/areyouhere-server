package com.waruru.areyouhere.course.advice;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;

import com.waruru.areyouhere.course.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.course.exception.ManagerNotFoundException;
import com.waruru.areyouhere.course.exception.UnauthorizedManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


@RestControllerAdvice("com.waruru.areyouhere.course")
public class CourseExceptionAdvice {
    @ExceptionHandler(ManagerNotFoundException.class)
    public ResponseEntity<?> handleManagerNotFoundException(ManagerNotFoundException ex, WebRequest request) {
        // 여기서 에러 응답을 구성하여 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<?> handleCourseNotFoundException(CourseNotFoundException ex, WebRequest request) {
        // 여기서 에러 응답을 구성하여 반환
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedManagerException.class)
    public ResponseEntity<?> handleUnauthorizedManagerException(UnauthorizedManagerException ex, WebRequest request) {
        // 여기서 에러 응답을 구성하여 반환
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }
    @ExceptionHandler(AttendeesNotUniqueException.class)
    public ResponseEntity<HttpStatus> handleAttendeesNotUniqueException(AttendeesNotUniqueException ex) {
        return RESPONSE_BAD_REQUEST;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        // 모든 나머지 예외들을 처리
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
    }
}
