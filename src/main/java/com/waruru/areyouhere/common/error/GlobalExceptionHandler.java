package com.waruru.areyouhere.common.error;

import com.waruru.areyouhere.common.annotation.SlackNotification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.gpedro.integrations.slack.SlackApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private final SlackApi slackApi;

  public GlobalExceptionHandler(@Value("${spring.slack.webhook}") String webhook) {
    this.slackApi = new SlackApi(webhook);
  }

  @SlackNotification
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  @Profile(value = {"develop", "prod"})
  public ErrorResponse handleException(HttpServletRequest request, Exception e) {
    log.error("Internal Server Error", e);
    StringBuilder sb = new StringBuilder();
    sb.append(request.getMethod());
    sb.append(request.getRemoteAddr());
    sb.append(request.getRequestURL());
    sb.append(e.getMessage());

    return ErrorResponse.of("INTERNAL SERVER ERROR", sb.toString());
  }



}
