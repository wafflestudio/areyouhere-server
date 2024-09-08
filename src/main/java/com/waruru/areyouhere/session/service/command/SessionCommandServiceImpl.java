package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.active.service.ActiveAttendanceService;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.ActivatedSessionExistsException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionCommandServiceImpl implements SessionCommandService {

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    private final ActiveAttendanceService activeAttendanceService;

    @Override
    public void create(Long managerId, Long courseId, String sessionName) {
        // TODO : exception 수정
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        throwIfCourseAuthorizationFail(managerId, courseId);
        sessionRepository.findMostRecentByCourseId(courseId)
                .ifPresent(session -> {
                    if (!session.isDeactivated()) {
                        throw new ActivatedSessionExistsException();
                    }
                });

        if (attendeeRepository.findAttendeesByCourse_Id(courseId).isEmpty()) {
            throw new AttendeeNotFoundException();
        }

        Session session = Session.builder()
                .name(sessionName)
                .course(course)
                .isDeactivated(false)
                .build();
        sessionRepository.save(session);
    }

    @Override
    public void deleteNotActivated(Long managerId, Long courseId) {
        throwIfCourseAuthorizationFail(managerId, courseId);

        sessionRepository.findMostRecentByCourseId(courseId)
                .ifPresent(session -> {
                    if (!activeAttendanceService.isSessionActivatedByCourseId(courseId) && !session.isDeactivated()) {
                        sessionRepository.delete(session);
                    }
                });
    }
    @Override
    public void deleteAll(Long managerId, List<Long> sessionIds) {

        sessionIds.forEach(sessionId -> throwIfCourseAuthorizationFail(managerId, sessionId));

        sessionIds.forEach(sessionId -> {
            attendanceRepository.deleteAllBySessionId(sessionId);
            sessionRepository.findById(sessionId).orElseThrow(CurrentSessionNotFoundException::new);
        });
        sessionRepository.deleteAllByIds(sessionIds);
    }

    @Override
    public void deactivate(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setDeactivated(true);
        sessionRepository.save(session);
    }

    @Override
    public void setAuthCodeDate(Session session, LocalDateTime date){
        session.setAuthCodeCreatedAt(date);
        sessionRepository.save(session);
    }

    @Override
    public void updateAll(Long managerId, List<UpdateSession> sessions) {
        sessions.forEach(session -> throwIfCourseAuthorizationFail(managerId, session.getId()));

        sessions.forEach(session -> {
            sessionRepository.setSessionNameById(session.getName(), session.getId());
        });
    }

    private void throwIfCourseAuthorizationFail(Long managerId, Long courseId){
        if(!courseRepository.isCourseMadeByManagerId(managerId, courseId)){
            throw new UnAuthenticatedException();
        }
    }

}
