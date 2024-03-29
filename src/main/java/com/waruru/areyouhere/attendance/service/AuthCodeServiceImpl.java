package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.domain.entity.AttendeeRedisData;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRedisRepository;
import com.waruru.areyouhere.attendance.exception.AlreadyAttendException;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.attendance.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionIdRedisRepository;
import com.waruru.areyouhere.attendance.exception.AuthCodeNotFoundException;
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
@Transactional
public class AuthCodeServiceImpl implements AuthCodeService{

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;
    private final AttendeeRepository attendeeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
    private final AttendanceRedisRepository attendanceRedisRepository;

    @Override
    public List<AttendeeInfo> hasNameSake(String authCode, String attendeeName){
        AuthCode authCodeData = authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        List<AttendeeRedisData> attendees = authCodeData.getAttendees();
        return attendees.stream()
                .filter(att -> att.getName().equals(attendeeName))
                .map(att -> AttendeeInfo.builder()
                        .name(att.getName())
                        .id(att.getId())
                        .note(att.getNote())
                        .build())
                .toList();
    }
    @Override
    public AuthCodeInfo isAttendPossible(String authCode, String attendeeName, Long attendeeId){

        AuthCode authCodeData = authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        AttendeeRedisData attendeeInfo = authCodeData.getAttendees().stream()
                .filter(att -> att.getName().equals(attendeeName))
                .findAny()
                .orElseThrow(StudentNameNotFoundException::new);

        if(attendeeId != null){
            attendeeInfo = authCodeData.getAttendees().stream()
                    .filter(att -> att.getId().equals(attendeeId))
                    .findAny()
                    .orElseThrow(StudentNameNotFoundException::new);
        }

        if(attendanceRedisRepository.isAlreadyAttended(authCode, attendeeInfo)){
            throw new AlreadyAttendException();
        }

        attendanceRedisRepository.setAttend(authCode, attendeeInfo);


        return AuthCodeInfo.builder()
                .courseName(authCodeData.getCourseName())
                .sessionName(authCodeData.getSessionName())
                .sessionId(authCodeData.getSessionId())
                .build();
    }

    @Override
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

        AuthCode authCode = AuthCode.builder()
                .sessionId(session.getId())
                .authCode(generatedAuthCode)
                .attendees(attendeesByCourseId)
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
    @Override
    public void deactivate(String authCode){
        AuthCode authCodeByAuthCode = authCodeRedisRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        authCodeRedisRepository.delete(authCodeByAuthCode);
        sessionIdRedisRepository.deleteById(authCodeByAuthCode.getSessionId());
        attendanceRedisRepository.deleteAllAttendanceInSession(authCode);

    }

}
