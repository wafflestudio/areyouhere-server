package com.waruru.areyouhere.attendee.domain.repository.dto;

import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import java.time.LocalDateTime;

public interface SessionAttendeeInfo {

    public Long getAttendanceId();

    public Long getAttendeeId();

    public String getAttendeeName();

    public String getAttendeeNote();

    public AttendanceType getAttendanceStatus();

    public LocalDateTime getAttendanceTime();

}
