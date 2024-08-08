package com.waruru.areyouhere.email.advice;


import com.waruru.areyouhere.common.annotation.SlackNotification;
import com.waruru.areyouhere.common.error.ErrorResponse;
import com.waruru.areyouhere.email.exception.EmailSendException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 이메일 전송이 안되는 것은 이메일 서버에 이상이 있으므로 slack에서 처리될 수 있도록 한다.
@RestControllerAdvice("com.waruru.areyouhere.email")
@Slf4j
public class EmailExceptionAdvice {

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
}
