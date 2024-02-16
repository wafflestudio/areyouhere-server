package com.waruru.areyouhere.session.service.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentSessionDto {
    private String authCode;
    private String sessionName;
    private LocalDateTime sessionTime;

    @Builder
    public CurrentSessionDto(String authCode, String sessionName, LocalDateTime sessionTime) {
        this.authCode = authCode;
        this.sessionName = sessionName;
        this.sessionTime = sessionTime;
    }
}
