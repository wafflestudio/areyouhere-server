package com.waruru.areyouhere.session.service.command;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionCommandService {
    public void create(Long courseId, String sessionName);
    public void delete(List<Long> sessionIds);
    public void deactivate(Long sessionId);
    public void setStartTime(Long sessionId, LocalDateTime currentTime);
}
