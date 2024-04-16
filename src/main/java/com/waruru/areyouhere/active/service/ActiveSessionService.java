package com.waruru.areyouhere.active.service;

public interface ActiveSessionService {

    public void deactivateSession(String authCode, Long sessionId, Long courseId);

    public String activateSession(Long sessionId, Long courseId);

}
