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

    int attendee;

    int absentee;

    Long id;

    @Builder
    public SessionAttendanceInfo(LocalDateTime date, String name, int attendee, int absentee, Long id) {
        this.date = date;
        this.name = name;
        this.attendee = attendee;
        this.absentee = absentee;
        this.id = id;
    }


}
