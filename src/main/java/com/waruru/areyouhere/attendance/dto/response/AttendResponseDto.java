package com.waruru.areyouhere.attendance.dto.response;

import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import java.time.LocalDateTime;
import java.util.List;
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

    private List<AttendeeInfo> attendeeNotes;

    @Builder
    public AttendResponseDto(String courseName, String sessionName, String attendanceName,
                             LocalDateTime attendanceTime, List<AttendeeInfo> attendeeNotes) {
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.attendanceName = attendanceName;
        this.attendanceTime = attendanceTime;
        this.attendeeNotes = attendeeNotes;
    }
}
