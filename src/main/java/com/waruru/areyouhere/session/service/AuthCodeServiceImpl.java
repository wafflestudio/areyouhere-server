package com.waruru.areyouhere.session.service;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.session.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import com.waruru.areyouhere.session.exception.StudentNameNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//TODO : 일관성있는 메소드명

@Service
@RequiredArgsConstructor
public class AuthCodeServiceImpl implements AuthCodeService{
    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionRepository sessionRepository;
    private final AttendeeRepository attendeeRepository;

    private final RandomIdentifierGenerator randomIdentifierGenerator;

    public Long checkAuthCodeAndGetSessionId(String authCode, String attendanceName){
        AuthCode authCodeData = authCodeRedisRepository
                .findAuthCodeByAuthCode(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        authCodeData.getAttendances().stream()
                .filter(att -> att.equals(attendanceName))
                .findAny()
                .orElseThrow(StudentNameNotFoundException::new);

        return authCodeData.getSessionId();
    }

//    public String createAuthCode(Long sessionId){
//        String generatedAuthCode = "";
//        while(true){
//            generatedAuthCode = randomIdentifierGenerator.generateRandomIdentifier(4);
//            Optional<AuthCode> authCodeData = authCodeRedisRepository
//                    .findAuthCodeByAuthCode(generatedAuthCode);
//
//            if(authCodeData.isEmpty())
//                break;
//        }
//
//        Session session = sessionRepository.findById(sessionId)
//                .orElseThrow(SessionIdNotFoundException::new);
//
//        if(session.isDeactivated()){
//            throw new CurrentSessionNotFoundException();
//        }
//
//        attendeeRepository.findAttendeesByCourse_Id(cou)
//        AuthCode authCode = AuthCode.builder()
//                .authCode(generatedAuthCode)
//                .sessionId(sessionId)
//                .attendances()
//                .build();
//        return generatedAuthCode;
//
//    }
}
