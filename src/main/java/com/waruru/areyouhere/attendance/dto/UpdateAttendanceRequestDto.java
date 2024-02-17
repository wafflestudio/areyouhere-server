package com.waruru.areyouhere.attendance.dto;

import java.util.List;
import lombok.Data;

@Data
public class UpdateAttendanceRequestDto {
    private List<UpdateAttendance> updateAttendances;
}
