package com.waruru.areyouhere.session.service.query;

import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.active.domain.repository.ActiveSessionRepository;
import com.waruru.areyouhere.session.domain.repository.ClassSessionRepository;
import com.waruru.areyouhere.session.domain.repository.dto.SessionInfo;
import com.waruru.areyouhere.session.exception.CurrentSessionDeactivatedException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionQueryServiceImpl implements SessionQueryService {

    private final ClassSessionRepository classSessionRepository;
    private final ActiveSessionRepository activeSessionRepository;

    @Override
    public CurrentSessionDto getCurrentSessionInfo(Long courseId) {
        Session mostRecentSession = classSessionRepository
                .findMostRecentByCourseId(courseId)
                .orElseThrow(CurrentSessionNotFoundException::new);

        // 제일 최근 세션이 이미 출석 체크가 끝났는지
        throwIfSessionDeactivated(mostRecentSession);

        // 제일 최근 세션이 출석 코드를 만들지 않았는지
        CurrentSessionAttendanceInfo currentSessionAttendanceInfo = getAuthCodeIfExistsOrEmptyAuthCode(
                mostRecentSession.getId());

        return CurrentSessionDto.builder()
                .authCode(currentSessionAttendanceInfo.getAuthCode())
                .sessionTime(currentSessionAttendanceInfo.getCreatedAt())
                .sessionName(mostRecentSession.getName())
                .id(mostRecentSession.getId())
                .build();
    }


    @Override
    public List<SessionAttendanceInfo> getRecentFive(Long courseId) {
        List<Session> recentSessions = getRecentSessions(courseId);
        removeExtraSession(recentSessions);
        return getSessionAttendanceInfoList(recentSessions);
    }


    @Override
    public List<SessionAttendanceInfo> getAll(Long courseId) {
        List<SessionInfo> allSessions = classSessionRepository.findSessionsWithAttendance(courseId);

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
    public SessionAttendanceInfo getSessionAttendanceInfo(Long sessionId) {
        SessionInfo sessionWithAttendance = classSessionRepository
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
    public void checkNotDeactivated(Long sessionId) {
        Session session = classSessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        throwIfSessionDeactivated(session);
    }

    @Override
    public Session get(Long sessionId) {
        return classSessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
    }

    private void throwIfSessionDeactivated(Session mostRecentSession) {
        if (mostRecentSession.isDeactivated()) {
            throw new CurrentSessionDeactivatedException();
        }
    }

    private CurrentSessionAttendanceInfo getAuthCodeIfExistsOrEmptyAuthCode(Long sessionId) {
        return activeSessionRepository
                .findBySessionId(sessionId).orElseGet(() -> CurrentSessionAttendanceInfo.builder()
                        .authCode(null)
                        .sessionName(null)
                        .build());
    }

    private List<Session> getRecentSessions(Long courseId) {
        return Optional.ofNullable(classSessionRepository.findTOP6ByCourseId(courseId))
                .orElse(Collections.emptyList());
    }

    private void removeExtraSession(List<Session> recentFiveSessions) {
        if (recentFiveSessions.isEmpty()) {
            return;
        }

        if (!recentFiveSessions.get(0).isDeactivated()) {
            recentFiveSessions.remove(0);
        } else if (recentFiveSessions.size() > 5) {
            recentFiveSessions.remove(5);
        }
    }

    //FIXME : 404 발생
    private List<SessionAttendanceInfo> getSessionAttendanceInfoList(List<Session> recentFiveSessions) {
        return recentFiveSessions.stream()
                .map(Session::getId)
                .map(sessionId -> classSessionRepository.findSessionWithAttendance(sessionId)
                        .orElseThrow(SessionIdNotFoundException::new))
                .map(sessionWithAttendance -> SessionAttendanceInfo.builder()
                        .attendee(sessionWithAttendance.getattendee())
                        .absentee(sessionWithAttendance.getabsentee())
                        .date(sessionWithAttendance.getdate())
                        .name(sessionWithAttendance.getname())
                        .id(sessionWithAttendance.getid())
                        .build())
                .collect(Collectors.toList());
    }


}
