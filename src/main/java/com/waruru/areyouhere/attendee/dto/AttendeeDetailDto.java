package com.waruru.areyouhere.attendee.dto;


import com.waruru.areyouhere.attendee.domain.repository.dto.AttendeeAttendDetailInfo;
import com.waruru.areyouhere.attendee.service.dto.AttendeeAttendanceInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendeeDetailDto {
    private AttendeeAttendanceInfo attendee;

    private List<AttendeeAttendeeDetail> attendanceInfo;

    @Builder
    public AttendeeDetailDto(AttendeeAttendanceInfo attendee, List<AttendeeAttendDetailInfo> attendanceInfo) {
        this.attendee = attendee;
        this.attendanceInfo = attendanceInfo.stream()
                .map(attendeeAttendDetailInfo -> new AttendeeAttendeeDetail(
                        attendeeAttendDetailInfo.getAttendanceId(),
                        attendeeAttendDetailInfo.getSessionName(),
                        attendeeAttendDetailInfo.getAttendanceStatus(),
                        attendeeAttendDetailInfo.getAttendanceTime().toString()
                ))
                .toList();
    }
}

record AttendeeAttendeeDetail(Long attendanceId, String sessionName, Boolean attendanceStatus, String attendanceTime) {
}

