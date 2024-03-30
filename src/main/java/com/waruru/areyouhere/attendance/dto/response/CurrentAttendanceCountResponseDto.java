package com.waruru.areyouhere.attendance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentAttendanceCountResponseDto {
    private int attendances;
    private int total;
}
