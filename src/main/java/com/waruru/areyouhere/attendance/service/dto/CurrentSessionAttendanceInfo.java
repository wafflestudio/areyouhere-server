package com.waruru.areyouhere.attendance.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentSessionAttendanceInfo {
    private List<String> attendees;
    private List<String> absentees;

    @Builder
    public CurrentSessionAttendanceInfo(List<String> attendees, List<String> absentees) {
        this.attendees = attendees;
        this.absentees = absentees;
    }
}
