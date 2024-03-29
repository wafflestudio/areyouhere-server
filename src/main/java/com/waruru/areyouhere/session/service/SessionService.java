package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {

    public void create(Long courseId, String sessionName);

    public CurrentSessionDto getCurrentSessionInfo(Long courseId);
    public List<SessionAttendanceInfo> getRecentFiveSessions(Long courseId);

    public List<SessionAttendanceInfo> getAllSessions(Long courseId);

    public SessionAttendanceInfo getSessionInfo(Long sessionId);

    public Session getSession(Long sessionId);

    public void delete(Long sessionId);

    public void checkSessionNotDeactivated(Long sessionId);

    public void deactivate(Long sessionId);

    public void setSessionStartTime(Long sessionId, LocalDateTime currentTime);

}
