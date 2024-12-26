package com.waruru.areyouhere.attendee.service.dto;


import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SessionAttendees {
    private AttendeeInfo attendee;
    
    private Long attendanceId;

    private AttendanceType attendanceStatus;

    private LocalDateTime attendanceTime;


    @Builder
    public SessionAttendees(Long attendanceId, Long attendeeId, String name, String note, AttendanceType attendanceStatus, LocalDateTime attendanceTime) {
        this.attendanceId = attendanceId;
        this.attendee = AttendeeInfo.builder()
                .id(attendeeId)
                .name(name)
                .note(note)
                .build();
        this.attendanceStatus = attendanceStatus;
        this.attendanceTime = attendanceTime;
    }
}
