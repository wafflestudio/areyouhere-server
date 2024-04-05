package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.domain.entity.AttendeeRedisData;
import com.waruru.areyouhere.attendance.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceRedisService {

    public List<AttendeeInfo> getNameSakeInfos(String authCode, String attendeeName);
    public AuthCodeInfo isAttendPossible(String authCode, String attendanceName, Long attendeeId);


    public String createAuthCode(Course course, Session sessionId, LocalDateTime currentTime);

    public void deactivate(String authCode);

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendanceInfo(String authCode);

    public void setAttendInRedis(String authCode, AttendeeRedisData attendeeInfo);

    public AttendeeRedisData getAttendeeInSession(String attendeeName, Long attendeeId,
                                                  CurrentSessionAttendanceInfo currentSessionAttendanceInfoData);

    public CurrentSessionAttendanceInfo getSessionAttendanceInfo(String authCode);
}
