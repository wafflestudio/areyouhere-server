package com.waruru.areyouhere.attendee.service.dto;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SessionAttendees {
    private AttendeeInfo attendeeInfo;

    private Long attendanceId;

    private boolean attendanceStatus;

    private LocalDateTime attendanceTime;


    @Builder
    public SessionAttendees(Long id, String name, String note, boolean attendanceStatus, LocalDateTime attendanceTime) {
        this.attendanceId = id;
        this.attendeeInfo = AttendeeInfo.builder()
                .name(name)
                .note(note)
                .build();
        this.attendanceStatus = attendanceStatus;
        this.attendanceTime = attendanceTime;
    }
}
