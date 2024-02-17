package com.waruru.areyouhere.session.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(Include.NON_NULL)
public class CurrentSessionDto {
    private String authCode;
    private String sessionName;
    private LocalDateTime sessionTime;
    private Long id;

    @Builder
    public CurrentSessionDto(String authCode, String sessionName, LocalDateTime sessionTime, Long id) {
        this.authCode = authCode;
        this.sessionName = sessionName;
        this.sessionTime = sessionTime;
        this.id = id;
    }
}
