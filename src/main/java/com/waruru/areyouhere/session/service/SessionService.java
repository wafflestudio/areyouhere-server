package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionService {

    public void create(Long courseId, String sessionName);

    public CurrentSessionDto getCurrentSessionInfo(Long courseId);
    public List<SessionAttendanceInfo> getRecentFive(Long courseId);

    public List<SessionAttendanceInfo> getAll(Long courseId);

    public SessionAttendanceInfo getSessionAttendanceInfo(Long sessionId);

    public Session get(Long sessionId);

    public void delete(List<Long> sessionId);

    public void checkNotDeactivated(Long sessionId);

    public void deactivate(Long sessionId);

    public void setStartTime(Long sessionId, LocalDateTime currentTime);

}
