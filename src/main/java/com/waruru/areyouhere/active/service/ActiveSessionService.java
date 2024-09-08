package com.waruru.areyouhere.active.service;

public interface ActiveSessionService {

    public void deactivate(String authCode, Long sessionId, Long courseId);

    public String activate(Long managerId, Long sessionId, Long courseId);

}
