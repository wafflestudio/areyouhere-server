package com.waruru.areyouhere.attendance.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentSessionAttendCount {
    private int total;
    private int attendanceCount;
}
