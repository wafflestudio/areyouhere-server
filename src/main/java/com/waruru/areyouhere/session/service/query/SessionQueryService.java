package com.waruru.areyouhere.session.service.query;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;

public interface SessionQueryService {
    public CurrentSessionDto getCurrentSessionInfo(Long managerId, Long courseId);

    public List<SessionAttendanceInfo> getRecentFive(Long managerId, Long courseId);

    public List<SessionAttendanceInfo> getAll(Long managerId, Long courseId);

    public SessionAttendanceInfo getSessionAttendanceInfo(Long managerId, Long sessionId);

    public void checkNotDeactivated(Long sessionId);

    public Session get(Long managerId, Long sessionId);

    public void throwIfSessionAuthorizationFail(Long managerId, Long sessionId);
}
