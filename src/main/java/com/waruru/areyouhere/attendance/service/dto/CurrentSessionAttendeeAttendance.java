package com.waruru.areyouhere.attendance.service.dto;

import com.waruru.areyouhere.attendance.domain.entity.AttendeeRedisData;
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

    @Builder
    public CurrentSessionAttendeeAttendance(List<AttendeeRedisData> attendees, List<AttendeeRedisData> absentees) {
        this.attendees = attendees;
        this.absentees = absentees;
    }
}
