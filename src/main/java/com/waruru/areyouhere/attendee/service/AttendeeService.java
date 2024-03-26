package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendee.dto.AttendeeData;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.List;

public interface AttendeeService {

    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId);

    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId);

    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId);

    public void createAttendees(Long courseId, List<AttendeeData> newAttendees);

    public void deleteAttendees(List<Long> deleteAttendees);

    public int getAttendeeByCourseId(Long courseId);

    public DuplicateAttendees getDuplicateAttendees(Long courseId, List<String> newAttendees);
}
