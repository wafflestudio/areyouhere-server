package com.waruru.areyouhere.attendance.service.command;

import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import java.util.List;

public interface AttendeeCommandService {
    public void createAll(Long courseId, List<AttendeeInfo> newAttendees);
    public void deleteAll(List<Long> deleteAttendees);

    public void updateAll(Long courseId, List<AttendeeInfo> updatedAttendees);
}
