package com.waruru.areyouhere.attendance.service.rdb;

import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import java.util.List;

public interface AttendanceRDBService {
    public void setAttendancesAfterDeactivate(long courseId, long sessionId, CurrentSessionAttendanceInfo currentSessionAttendanceInfo);

    public void setAttendanceStatuses(Long sessionId, List<UpdateAttendance> updateAttendances);
}
