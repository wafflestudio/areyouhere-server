package com.waruru.areyouhere.attendance.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCodeDeactivationRequestDto {

    private String authCode;
    private Long sessionId;
    private Long courseId;

}
