package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import java.util.List;

public interface AttendanceRDBService {
    public void setAbsentAfterDeactivation(long courseId, long sessionId);

    public void setAttend(Long sessionId, String attendanceName, Long attendeeId);

    public void setAttendanceStatuses(Long sessionId , List<UpdateAttendance> updateAttendances);

    public int currentAttendance(Long sessionId);
}
