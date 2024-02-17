package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;

public interface AttendanceService {
    public AttendanceCount getAttendanceCount(long sessionId);
    public void setAbsentAfterDeactivation(long courseId, long sessionId);

    public void setAttend(Long sessionId, String attendanceName);

    public void setAttendanceStatuses(UpdateAttendanceRequestDto updateAttendanceRequestDto);
    public int currentAttendance(Long sessionId);

}
