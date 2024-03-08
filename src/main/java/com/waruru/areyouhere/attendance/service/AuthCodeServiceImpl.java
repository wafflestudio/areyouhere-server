package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.session.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionIdRedisRepository;
import com.waruru.areyouhere.session.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.session.exception.StudentNameNotFoundException;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO : 일관성있는 메소드명

@Service
@RequiredArgsConstructor
public class AuthCodeServiceImpl implements AuthCodeService{

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;
    private final AttendeeRepository attendeeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;

    @Transactional
    public AuthCodeInfo checkAuthCodeAndGetSessionId(String authCode, String attendanceName){
        AuthCode authCodeData = authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        authCodeData.getAttendances().stream()
                .filter(att -> att.equals(attendanceName))
                .findAny()
                .orElseThrow(StudentNameNotFoundException::new);

        return AuthCodeInfo.builder()
                .courseName(authCodeData.getCourseName())
                .sessionName(authCodeData.getSessionName())
                .sessionId(authCodeData.getSessionId())
                .build();
    }

    @Transactional
    public String createAuthCode(Course course, Session session, LocalDateTime currentTime){
        String generatedAuthCode = "";
        while(true){
            generatedAuthCode = randomIdentifierGenerator.generateRandomIdentifier(4);
            Optional<AuthCode> authCodeData = authCodeRedisRepository
                    .findById(generatedAuthCode);

            if(authCodeData.isEmpty())
                break;
        }

        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(course.getId());
        List<String> attendees = attendeesByCourseId.stream()
                .map(Attendee::getName)
                .toList();

        AuthCode authCode = AuthCode.builder()
                .sessionId(session.getId())
                .authCode(generatedAuthCode)
                .attendances(attendees)
                .sessionName(session.getName())
                .courseName(course.getName())
                .createdAt(currentTime.toString())
                .build();

        authCodeRedisRepository.save(authCode);

        SessionId newsessionId = SessionId.builder()
                .authCode(authCode.getAuthCode())
                .sessionId(session.getId())
                .build();

        sessionIdRedisRepository.save(newsessionId);

        return generatedAuthCode;
    }
    // TODO : sessionId 검증, 해당 sessionId가 user 소유인지 검증.
    @Transactional
    public void deactivate(String authCode){
        AuthCode authCodeByAuthCode = authCodeRedisRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        authCodeRedisRepository.delete(authCodeByAuthCode);
        sessionIdRedisRepository.deleteById(authCodeByAuthCode.getSessionId());

    }
}
