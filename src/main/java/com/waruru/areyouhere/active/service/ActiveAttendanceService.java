package com.waruru.areyouhere.active.service;

import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.util.List;

public interface ActiveAttendanceService {

    public List<AttendeeInfo> getNameSakeInfos(String authCode, String attendeeName);

    public AuthCodeInfo isAttendPossible(String authCode, String attendanceName, Long attendeeId);

    public String activate(Course course, Session sessionId, LocalDateTime currentTime);

    public void deactivate(String authCode);

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendees(String authCode);

    public void setAttendInRedis(String authCode, AttendeeRedisData attendeeInfo);

    public AttendeeRedisData findByNameIfNotDuplicatedOrId(String attendeeName, Long attendeeId,
                                                           CurrentSessionAttendanceInfo currentSessionAttendanceInfoData);

    public CurrentSessionAttendanceInfo getSessionAttendanceInfoOrThrow(String authCode);

    public String findAuthCodeBySessionId(Long sessionId);

    public int getTotalAttendees(String authCode);

    public int getAttendCount(String authCode);

    public void updateCourseName(Long courseId, String courseName);


    public void updateAttendees(Long courseId, List<Attendee> attendees);

    public boolean isSessionActivatedByCourseId(Long courseId);


}
