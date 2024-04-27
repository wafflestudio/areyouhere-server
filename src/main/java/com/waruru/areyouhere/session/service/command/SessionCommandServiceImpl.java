package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.active.service.ActiveAttendanceService;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.ClassSessionRepository;
import com.waruru.areyouhere.session.exception.ActivatedSessionExistsException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionCommandServiceImpl implements SessionCommandService {

    private final ClassSessionRepository classSessionRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    private final ActiveAttendanceService activeAttendanceService;

    @Override
    public void create(Long courseId, String sessionName) {
        // TODO : exception 수정
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        classSessionRepository.findMostRecentByCourseId(courseId)
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
        classSessionRepository.save(session);
    }

    @Override
    public void deleteNotActivated(Long courseId) {
        classSessionRepository.findMostRecentByCourseId(courseId)
                .ifPresent(session -> {
                    if (!activeAttendanceService.isSessionActivatedByCourseId(courseId) && !session.isDeactivated()) {
                        classSessionRepository.delete(session);
                    }
                });
    }
    @Override
    public void deleteAll(List<Long> sessionIds) {
        sessionIds.forEach(sessionId -> {
            attendanceRepository.deleteAllBySessionId(sessionId);
            classSessionRepository.findById(sessionId).orElseThrow(CurrentSessionNotFoundException::new);
        });
        classSessionRepository.deleteAllByIds(sessionIds);
    }

    @Override
    public void deactivate(Long sessionId) {
        Session session = classSessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setDeactivated(true);
        classSessionRepository.save(session);
    }


    @Override
    public void updateAll(List<UpdateSession> sessions) {
        sessions.forEach(session -> {
            classSessionRepository.setSessionNameById(session.getName(), session.getId());
        });
    }


}
