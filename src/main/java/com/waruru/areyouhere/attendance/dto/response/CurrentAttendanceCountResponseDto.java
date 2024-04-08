package com.waruru.areyouhere.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentAttendanceCountResponseDto {
    private int total;
    private int attendances;
}
