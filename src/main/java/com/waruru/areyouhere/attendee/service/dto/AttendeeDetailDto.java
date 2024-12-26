package com.waruru.areyouhere.attendee.service.dto;


import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.dto.AttendeeAttendDetailInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendeeDetailDto {
    private AttendeeInfo attendee;
    private int attendance;
    private int absence;
    private int late;

    private List<AttendeeAttendeeDetail> attendanceInfo;

    @Builder
    public AttendeeDetailDto(Attendee attendee, int attendance, int absence, int late, List<AttendeeAttendDetailInfo> attendanceInfo) {
        this.attendee = AttendeeInfo.builder()
                .id(attendee.getId())
                .name(attendee.getName())
                .note(attendee.getNote())
                .build();
        this.attendance = attendance;
        this.absence = absence;
        this.late = late;
        this.attendanceInfo = attendanceInfo.stream()
                .map(attendeeAttendDetailInfo -> new AttendeeAttendeeDetail(
                        attendeeAttendDetailInfo.getAttendanceId(),
                        attendeeAttendDetailInfo.getSessionId(),
                        attendeeAttendDetailInfo.getSessionName(),
                        attendeeAttendDetailInfo.getAttendanceStatus(),
                        attendeeAttendDetailInfo.getAttendanceTime().toString()
                ))
                .toList();
    }
}

record AttendeeAttendeeDetail(Long attendanceId, Long sessionId, String sessionName, AttendanceType attendanceStatus, String attendanceTime) {
}

