package com.waruru.areyouhere.attendee.service.dto;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class SessionAttendees {

    private Long attendanceId;

    private String attendeeName;

    private boolean attendanceStatus;

    private LocalDateTime attendanceTime;


    @Builder
    public SessionAttendees(Long attendanceId, String attendeeName, boolean attendanceStatus, LocalDateTime attendanceTime) {
        this.attendanceId = attendanceId;
        this.attendeeName = attendeeName;
        this.attendanceStatus = attendanceStatus;
        this.attendanceTime = attendanceTime;
    }
}
