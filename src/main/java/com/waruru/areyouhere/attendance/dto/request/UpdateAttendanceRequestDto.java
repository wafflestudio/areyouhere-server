package com.waruru.areyouhere.attendance.dto.request;

import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import java.util.List;
import lombok.Data;

@Data
public class UpdateAttendanceRequestDto {
    private Long sessionId;
    private List<UpdateAttendance> updateAttendances;
}
