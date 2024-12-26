package com.waruru.areyouhere.attendee.domain.repository.dto;

import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import java.time.LocalDateTime;

public interface AttendeeAttendDetailInfo {
    public Long getAttendanceId();

    public Long getSessionId();

    public String getSessionName();

    public AttendanceType getAttendanceStatus();

    public LocalDateTime getAttendanceTime();
}
