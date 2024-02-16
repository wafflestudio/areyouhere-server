package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.session.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO : user 정보에 따른 course가 맞는지 확인하는 로직 추가. 보안.
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final AuthCodeRedisRepository authCodeRedisRepository;

    @Transactional
    public void createSession(Long courseId, String sessionName){
        // TODO: courseId 존재 여부 검증
//        Session session = Session.builder()
//                .course(TODO : course 조회)
//                .sessionName(sessionName)
//                .build();

    }
    // TODO : 리팩토링
    @Transactional(readOnly = true)
    @Override
    public CurrentSessionDto getCurrentSessionInfo(Long courseId){
        Session mostRecentSession = sessionRepository
                .findMostRecentSessionByCourseId(courseId)
                .orElseThrow(CurrentSessionNotFoundException::new);

        AuthCode authCode = authCodeRedisRepository
                .findAuthCodeBySessionId(mostRecentSession.getId())
                .orElseThrow(CurrentSessionNotFoundException::new);

        return CurrentSessionDto.builder()
                .authCode(authCode.getAuthCode())
                .sessionTime(mostRecentSession.getCreatedAt())
                .sessionName(mostRecentSession.getName())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getRecentFiveSessions(Long ManagerId, Long courseId){
        List<Session> recentFiveSessions = sessionRepository.findTOP5BySessionByCourseId(courseId);
        return recentFiveSessions == null || recentFiveSessions.isEmpty()
                ? Collections.emptyList()
                : recentFiveSessions;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Session> getAllSessions(Long ManagerId, Long courseId){
        List<Session> allSessions = sessionRepository.findAllByCourseId(courseId);
        return allSessions == null || allSessions.isEmpty()
                ? Collections.emptyList()
                : allSessions;
    }

    @Transactional(readOnly = true)
    @Override
    public Session getSession(Long ManagerId, Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
    }

    @Transactional
    @Override
    public void deleteSession(Long ManagerId, Long sessionId){
        sessionRepository.deleteById(sessionId);
    }


}
