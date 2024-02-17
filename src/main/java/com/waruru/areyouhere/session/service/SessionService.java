package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;

public interface SessionService {

    public CurrentSessionDto getCurrentSessionInfo(Long courseId);
    public List<Session> getRecentFiveSessions(Long ManagerId, Long courseId);

    public List<SessionAttendanceInfo> getAllSessions(Long ManagerId, Long courseId);

    public Session getSession(Long ManagerId, Long sessionId);

    public void deleteSession(Long ManagerId, Long sessionId);

    public SessionAttendanceInfo getSessions(Long sessionId);

}
