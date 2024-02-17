package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.List;

public interface AttendeeService {

    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId);

    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId);

    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId);
}
