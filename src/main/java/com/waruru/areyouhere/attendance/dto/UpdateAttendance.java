package com.waruru.areyouhere.attendance.dto;

import lombok.Data;

@Data
public class UpdateAttendance {
    private Long attendanceId;

    private boolean attendanceStatus;
}
