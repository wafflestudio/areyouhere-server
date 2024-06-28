package com.waruru.areyouhere.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public record ErrorResponse(
        String code,
        String message
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }

}
