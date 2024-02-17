package com.waruru.areyouhere.session.service.dto;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionAttendanceInfo {
    LocalDateTime date;

    String name;

    int attendees;

    int absentees;

    @Builder
    public SessionAttendanceInfo(LocalDateTime date, String name, int attendees, int absentees) {
        this.date = date;
        this.name = name;
        this.attendees = attendees;
        this.absentees = absentees;
    }


}