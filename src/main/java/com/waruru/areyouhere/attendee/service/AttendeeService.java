package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendee.service.dto.AttendeeDetailDto;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.List;

public interface AttendeeService {

    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId);

    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId);

    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId);

    public void createAll(Long courseId, List<AttendeeInfo> newAttendees);

    public void deleteAll(List<Long> deleteAttendees);

    public int getAllByCourseId(Long courseId);

    public DuplicateAttendees getDuplicatesAll(Long courseId, List<String> newAttendees);

    public void updateAll(Long courseId, List<AttendeeInfo> updatedAttendees);
}
