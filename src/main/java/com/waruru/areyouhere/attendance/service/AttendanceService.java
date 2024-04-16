package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.response.AttendResponseDto;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendCount;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import java.util.List;

public interface AttendanceService {

    public AttendResponseDto attend(String attendeeName, String authCode, Long attendeeId);

    public void updateAllStatuses(Long sessionId, List<UpdateAttendance> updateAttendances);

    public CurrentSessionAttendCount getCurrentSessionAttendCount(Long sessionId);

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendeesAndAbsentees(String authCode);

    public String activateSession(Long sessionId, Long courseId);

    public void deactivateSession(String authCode, Long sessionId, Long courseId);
}
