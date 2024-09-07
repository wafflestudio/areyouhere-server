package com.waruru.areyouhere.common.error;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_NOT_FOUND;

import com.waruru.areyouhere.common.annotation.SlackNotification;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<HttpStatus> handle404Exception(HttpServletRequest request, Exception e){
    return RESPONSE_NOT_FOUND;
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


  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({HandlerMethodValidationException.class, MethodArgumentNotValidException.class})
  public Map<String, String> handleValidationExceptions(
          MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }


}
