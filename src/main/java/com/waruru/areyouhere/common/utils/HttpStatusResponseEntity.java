package com.waruru.areyouhere.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpStatusResponseEntity {
    public static final ResponseEntity<HttpStatus> RESPONSE_OK = ResponseEntity.status(HttpStatus.OK).build();

    public static final ResponseEntity<HttpStatus> RESPONSE_NO_CONTENT = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_CONFLICT = ResponseEntity.status(HttpStatus.CONFLICT).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_BAD_REQUEST = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_NOT_FOUND = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    public static final ResponseEntity<HttpStatus> RESPONSE_FORBIDDEN = ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    public static final ResponseEntity<HttpStatus> RESPONSE_INTERNAL_ERROR = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
}
