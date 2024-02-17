package com.waruru.areyouhere.attendee.domain.repository.dto;

import java.time.LocalDateTime;

public interface SessionAttendeeInfo {
    public String getAttendeeName();

    public boolean getAttendanceStatus();

    public LocalDateTime getAttendanceTime();

}
