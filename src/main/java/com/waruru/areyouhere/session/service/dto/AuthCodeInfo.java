package com.waruru.areyouhere.session.service.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCodeInfo {
    private String courseName;
    private String sessionName;
    private Long sessionId;

    @Builder
    public AuthCodeInfo(String courseName, String sessionName, Long sessionId) {
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.sessionId = sessionId;
    }
}
