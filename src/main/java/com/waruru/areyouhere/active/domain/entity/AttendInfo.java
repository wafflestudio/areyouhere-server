package com.waruru.areyouhere.active.domain.entity;

import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttendInfo{
    LocalDateTime attendTime;
    AttendanceType attendanceType;
}
