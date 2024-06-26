package com.waruru.areyouhere.attendee.domain.repository.dto;

import java.time.LocalDateTime;

public interface AttendeeAttendDetailInfo {
    public Long getAttendanceId();

    public Long getSessionId();

    public String getSessionName();

    public Boolean getAttendanceStatus();

    public LocalDateTime getAttendanceTime();
}
