package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.session.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionIdRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.domain.repository.dto.SessionInfo;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO : user 정보에 따른 course가 맞는지 확인하는 로직 추가. 보안.
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;
    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void create(Long courseId, String sessionName){
        // TODO : exception 수정
        Course course = courseRepository.findById(courseId)
                .orElseThrow(SessionIdNotFoundException::new);

        Session session = Session.builder()
                .name(sessionName)
                .course(course)
                .isDeactivated(false)
                .build();
        sessionRepository.save(session);
    }

    @Transactional
    public void delete(Long sessionId){
        attendanceRepository.deleteAllBySessionId(sessionId);
        sessionRepository.findById(sessionId).orElseThrow(CurrentSessionNotFoundException::new);
        sessionRepository.deleteById(sessionId);
    }
    // TODO : 리팩토링 노타임..
    @Transactional(readOnly = true)
    @Override
    public CurrentSessionDto getCurrentSessionInfo(Long courseId){
        Session mostRecentSession = sessionRepository
                .findMostRecentSessionByCourseId(courseId)
                .orElseThrow(CurrentSessionNotFoundException::new);
        // 제일 최근 세션이 이미 출석 체크가 끝났는지
        if(mostRecentSession.isDeactivated()){
            throw new CurrentSessionDeactivatedException();
        }
        // 제일 최근 세션이 출석 코드를 만들지 않았는지
        SessionId sessionId = sessionIdRedisRepository
                .findById(mostRecentSession.getId())
                .orElse(null);

        if(sessionId == null){
            return  CurrentSessionDto.builder()
                            .authCode(null)
                            .sessionTime(null)
                            .sessionName(mostRecentSession.getName())
                            .id(mostRecentSession.getId())
                            .build();
        }
        // 제일 최근 세션이 출석 코드를 만들었다면.
        // warning! 널 익셉션이 발생한다면 authCode를 redis에 삽입하는 과정에서 어느 쪽이 빠져있는 것이다.

        AuthCode authCode = authCodeRedisRepository
                .findById(sessionId.getAuthCode())
                .orElse(null);

        return CurrentSessionDto.builder()
                .authCode(authCode.getAuthCode())
                .sessionTime(LocalDateTime.parse(authCode.getCreatedAt()))
                .sessionName(mostRecentSession.getName())
                .id(mostRecentSession.getId())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionAttendanceInfo> getRecentFiveSessions(Long courseId){
        List<Session> recentFiveSessions = sessionRepository.findTOP6BySessionByCourseId(courseId);
        if(recentFiveSessions == null || recentFiveSessions.isEmpty()){
            return Collections.emptyList();
        }

        if(!recentFiveSessions.get(0).isDeactivated()){
            recentFiveSessions.remove(0);

        }else{
            if(recentFiveSessions.size() > 5){
                recentFiveSessions.remove(5);
            }
        }
        List<SessionAttendanceInfo> list = new ArrayList<>();
        for (Session recentFiveSession : recentFiveSessions) {
            SessionInfo sessionWithAttendance = sessionRepository.findSessionWithAttendance(
                    recentFiveSession.getId())
                    .orElseThrow(SessionIdNotFoundException::new);

            list.add(SessionAttendanceInfo.builder()
                    .attendee(sessionWithAttendance.getattendee())
                    .absentee(sessionWithAttendance.getabsentee())
                    .date(sessionWithAttendance.getdate())
                    .name(sessionWithAttendance.getname())
                    .id(sessionWithAttendance.getid())
                    .build()
            );
        }
        return list;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SessionAttendanceInfo> getAllSessions(Long courseId){
        List<SessionInfo> allSessions = sessionRepository.findSessionsWithAttendance(courseId);
        return allSessions == null || allSessions.isEmpty()
                ? Collections.emptyList()
                : allSessions.stream().map(allSession -> SessionAttendanceInfo.builder()
                        .id(allSession.getid())
                        .name(allSession.getname())
                        .date(allSession.getdate())
                        .attendee(allSession.getattendee())
                        .absentee(allSession.getabsentee())
                        .build()).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public SessionAttendanceInfo getSessionInfo(Long sessionId){
        SessionInfo sessionWithAttendance = sessionRepository
                .findSessionWithAttendance(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);

        return SessionAttendanceInfo.builder()
                .id(sessionWithAttendance.getid())
                .name(sessionWithAttendance.getname())
                .date(sessionWithAttendance.getdate())
                .attendee(sessionWithAttendance.getattendee())
                .absentee(sessionWithAttendance.getabsentee())
                .build();

    }

    @Transactional(readOnly = true)
    @Override
    public void checkSessionNotDeactivated(Long sessionId){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        if(session.isDeactivated()){
            throw new CurrentSessionDeactivatedException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Session getSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
    }

    @Transactional
    @Override
    public void deactivate(Long sessionId){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setDeactivated(true);
        sessionRepository.save(session);
    }

    @Transactional
    public void setSessionStartTime(Long sessionId, LocalDateTime currentTime){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        session.setAuthCodeCreatedAt(currentTime);
        sessionRepository.save(session);
    }


}
