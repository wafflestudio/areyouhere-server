package com.waruru.areyouhere.active.service;

import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendance.service.rdb.AttendanceRDBService;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.service.CourseService;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.command.SessionCommandService;
import com.waruru.areyouhere.session.service.query.SessionQueryService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActiveSessionServiceImpl implements ActiveSessionService {

    private final CourseService courseService;
    private final SessionQueryService sessionQueryService;
    private final SessionCommandService sessionCommandService;
    private final AttendanceRDBService attendanceRDBService;
    private final ActiveAttendanceService activeAttendanceService;

    @Override
    @Transactional
    public String activate(Long managerId, Long sessionId, Long courseId) {
        Course course = courseService.get(managerId, courseId);
        Session session = sessionQueryService.get(managerId, sessionId);
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        sessionCommandService.setAuthCodeDate(session, currentTime);
        return activeAttendanceService.activate(course, session, currentTime);
    }

    @Override
    @Transactional
    public void deactivate(String authCode, Long sessionId, Long courseId) {
        sessionQueryService.checkNotDeactivated(sessionId);

        CurrentSessionAttendanceInfo currentSessionAttendanceInfo = activeAttendanceService.getSessionAttendanceInfoOrThrow(
                authCode);
        attendanceRDBService.setAttendancesAfterDeactivate(courseId, sessionId, currentSessionAttendanceInfo);
        sessionCommandService.deactivate(sessionId);
        activeAttendanceService.deactivate(authCode);
    }


}
