package com.waruru.areyouhere.attendance.service.rdb;

import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import java.util.List;

public interface AttendanceRDBService {
    public void setAttendancesAfterDeactivate(long courseId, long sessionId, CurrentSessionAttendeeAttendance currentSessionAttendeeAttendance);

    public void setAttendanceStatuses(Long sessionId, List<UpdateAttendance> updateAttendances);

    public int currentAttendance(Long sessionId);
}
