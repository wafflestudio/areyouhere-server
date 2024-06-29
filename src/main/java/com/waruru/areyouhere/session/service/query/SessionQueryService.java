package com.waruru.areyouhere.session.service.query;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;

public interface SessionQueryService {
    public CurrentSessionDto getCurrentSessionInfo(Long courseId);

    public List<SessionAttendanceInfo> getRecentFive(Long courseId);

    public List<SessionAttendanceInfo> getAll(Long courseId);

    public SessionAttendanceInfo getSessionAttendanceInfo(Long sessionId);

    public void checkNotDeactivated(Long sessionId);

    public Session get(Long sessionId);
}
