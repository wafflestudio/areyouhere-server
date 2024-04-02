package com.waruru.areyouhere.session.service.command;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionCommandServiceImpl implements SessionCommandService{

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    public void create(Long courseId, String sessionName){
        // TODO : exception 수정
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        if(attendeeRepository.findAttendeesByCourse_Id(courseId).isEmpty()){
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
    public void delete(List<Long> sessionIds){
        sessionIds.forEach(sessionId -> {
            attendanceRepository.deleteAllBySessionId(sessionId);
            sessionRepository.findById(sessionId).orElseThrow(CurrentSessionNotFoundException::new);
        });
        sessionRepository.deleteAllByIds(sessionIds);
    }

    @Override
    public void deactivate(Long sessionId){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setDeactivated(true);
        sessionRepository.save(session);
    }

    public void setStartTime(Long sessionId, LocalDateTime currentTime){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setAuthCodeCreatedAt(currentTime);
        sessionRepository.save(session);
    }


}
