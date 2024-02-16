package com.waruru.areyouhere.session.dto;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentSessionResponseDto {

    private String authCode;
    private String sessionName;
    private LocalDateTime sessionTime;

    @Builder
    public CurrentSessionResponseDto(String authCode, int totalAttendees, int currentAttendees, String sessionName, LocalDateTime sessionTime) {
        this.authCode = authCode;
        this.sessionName = sessionName;
        this.sessionTime = sessionTime;
    }
}
