package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionCommandService {
    public void create(Long courseId, String sessionName);

    public void deleteAll(List<Long> sessionIds);

    public void deactivate(Long sessionId);

    public void setStartTime(Long sessionId, LocalDateTime currentTime);

    public void updateAll(List<UpdateSession> sessions);
}
