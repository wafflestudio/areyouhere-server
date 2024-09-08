package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionCommandService {
    public void create(Long managerId, Long courseId, String sessionName);

    public void deleteNotActivated(Long managerId, Long courseId);

    public void deleteAll(Long managerId, List<Long> sessionIds);

    public void deactivate(Long sessionId);

    public void setAuthCodeDate(Session session, LocalDateTime date);

    public void updateAll(Long managerId, List<UpdateSession> sessions);
}
