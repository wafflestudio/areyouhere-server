package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import java.util.List;

public interface SessionService {

    public CurrentSessionDto getCurrentSessionInfo(Long courseId);
    public List<Session> getRecentFiveSessions(Long ManagerId, Long courseId);

    public List<Session> getAllSessions(Long ManagerId, Long courseId);

    public Session getSession(Long ManagerId, Long sessionId);

    public void deleteSession(Long ManagerId, Long sessionId);

}
