package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;

public interface AttendanceService {
    public AttendanceCount getAttendanceCount(long sessionId);
    public void setAbsentAfterDeactivation(long courseId, long sessionId);
}
