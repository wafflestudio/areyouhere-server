package com.waruru.areyouhere.attendance.dto;

import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import lombok.Data;

@Data
public class UpdateAttendance {
    private Long attendanceId;

    private AttendanceType attendanceStatus;
}
