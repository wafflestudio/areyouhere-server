package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.attendance.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionIdRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.domain.repository.dto.SessionInfo;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionQueryServiceImpl implements SessionQueryService{

    private final SessionRepository sessionRepository;
    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;

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

    @Override
    public List<SessionAttendanceInfo> getRecentFive(Long courseId){
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

    @Override
    public List<SessionAttendanceInfo> getAll(Long courseId){
        List<SessionInfo> allSessions = sessionRepository.findSessionsWithAttendance(courseId);
        return allSessions == null || allSessions.isEmpty()
                ? Collections.emptyList()
                : allSessions.stream().map(allSession -> SessionAttendanceInfo.builder()
                        .id(allSession.getid())
                        .name(allSession.getname())
                        .date(allSession.getdate())
                        .attendee(allSession.getattendee())
                        .absentee(allSession.getabsentee())
                        .build()
                ).toList();
    }

    @Override
    public SessionAttendanceInfo getSessionAttendanceInfo(Long sessionId){
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

    @Override
    public void checkNotDeactivated(Long sessionId){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        if(session.isDeactivated()){
            throw new CurrentSessionDeactivatedException();
        }
    }

    @Override
    public Session get(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
    }

}
