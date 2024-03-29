package com.waruru.areyouhere.attendee.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AttendeeAttendanceInfo {
    private String name;
    private String note;
    private int attendance;
    private int absence;
}
