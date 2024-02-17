package com.waruru.areyouhere.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentAttendanceCount {
    private int attendances;
    private int total;
}
