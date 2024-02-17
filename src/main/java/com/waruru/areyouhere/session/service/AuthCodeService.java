package com.waruru.areyouhere.session.service;

public interface AuthCodeService {
    public Long checkAuthCodeAndGetSessionId(String authCode, String attendanceName);


    public String createAuthCode(Long courseId, Long sessionId);

    public void deactivate(String authCode);
}
