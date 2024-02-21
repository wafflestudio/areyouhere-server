package com.waruru.areyouhere.session.service;

import java.time.LocalDateTime;

public interface AuthCodeService {
    public Long checkAuthCodeAndGetSessionId(String authCode, String attendanceName);


    public String createAuthCode(Long courseId, Long sessionId, LocalDateTime currentTime);

    public void deactivate(String authCode);
}
