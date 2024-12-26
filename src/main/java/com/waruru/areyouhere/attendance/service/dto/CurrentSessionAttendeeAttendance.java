package com.waruru.areyouhere.attendance.service.dto;

import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentSessionAttendeeAttendance {
    private List<AttendeeRedisData> attendees;
    private List<AttendeeRedisData> absentees;
    private List<AttendeeRedisData> lateness;

    @Builder
    public CurrentSessionAttendeeAttendance(List<AttendeeRedisData> attendees, List<AttendeeRedisData> absentees, List<AttendeeRedisData> lateness) {
        this.attendees = attendees;
        this.absentees = absentees;
        this.lateness = lateness;
    }
}
