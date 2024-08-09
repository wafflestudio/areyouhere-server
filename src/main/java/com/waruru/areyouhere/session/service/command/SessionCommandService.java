package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionCommandService {
    public void create(Long courseId, String sessionName);

    public void deleteNotActivated(Long courseId);

    public void deleteAll(List<Long> sessionIds);

    public void deactivate(Long sessionId);

    public void setAuthCodeDate(Session session, LocalDateTime date);

    public void updateAll(List<UpdateSession> sessions);
}
