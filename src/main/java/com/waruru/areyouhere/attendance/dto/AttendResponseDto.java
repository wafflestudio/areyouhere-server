package com.waruru.areyouhere.attendance.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendResponseDto {
    private String courseName;
    private String sessionName;
    private String attendanceName;
    private LocalDateTime attendanceTime;

    @Builder
    public AttendResponseDto(String courseName, String sessionName, String attendanceName,
                             LocalDateTime attendanceTime) {
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.attendanceName = attendanceName;
        this.attendanceTime = attendanceTime;
    }
}
