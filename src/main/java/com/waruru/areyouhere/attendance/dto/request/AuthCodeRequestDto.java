package com.waruru.areyouhere.attendance.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthCodeRequestDto {
    @NotNull
    private Long courseId;
    @NotNull
    private Long sessionId;
}
