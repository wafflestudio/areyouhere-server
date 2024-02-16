package com.waruru.areyouhere.attendance.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttendanceCount {
    private int attendees;
    private int absentees;
}
