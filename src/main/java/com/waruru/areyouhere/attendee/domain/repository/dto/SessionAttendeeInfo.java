package com.waruru.areyouhere.attendee.domain.repository.dto;

import java.time.LocalDateTime;

public interface SessionAttendeeInfo {

    public Long getAttendeeId();
    public String getAttendeeName();

    public Boolean getAttendanceStatus();

    public LocalDateTime getAttendanceTime();

}
