package com.waruru.areyouhere.attendee.service.command;

import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import java.util.List;

public interface AttendeeCommandService {
    public void createAll(Long managerId, Long courseId, List<AttendeeInfo> newAttendees);
    public void deleteAll(Long managerId, List<Long> deleteAttendees);

    public void updateAll(Long managerId, Long courseId, List<AttendeeInfo> updatedAttendees);
}
