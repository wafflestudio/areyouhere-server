package com.waruru.areyouhere.attendee.domain.repository.dto;

import java.time.LocalDateTime;

public interface SessionAttendeeInfo {

    public Long getAttendanceId();
    public String getAttendeeName();

    public String getAttendeeNote();

    public Boolean getAttendanceStatus();

    public LocalDateTime getAttendanceTime();

}
