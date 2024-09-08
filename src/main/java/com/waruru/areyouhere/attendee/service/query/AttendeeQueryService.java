package com.waruru.areyouhere.attendee.service.query;

import com.waruru.areyouhere.attendee.service.dto.AttendeeDetailDto;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.List;

public interface AttendeeQueryService {
    public AttendeeDetailDto getAttendanceCount(Long attendeeId);
    public DuplicateAttendees getDuplicatesAll(Long courseId, List<String> newAttendees);
    public DuplicateAttendees getAlreadyExists(Long courseId, List<String> newAttendees, DuplicateAttendees duplicateAttendees);
    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId);

    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId);

    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long managerId, Long courseId);

    public int getAllByCourseId(Long courseId);

}
