package com.waruru.areyouhere.session.dto.response;


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
    private Long id;

    @Builder
    public CurrentSessionResponseDto(String authCode, String sessionName, LocalDateTime sessionTime, Long id) {
        this.authCode = authCode;
        this.sessionName = sessionName;
        this.sessionTime = sessionTime;
        this.id = id;
    }
}
