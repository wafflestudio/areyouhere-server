package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.domain.entity.AttendeeRedisData;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRedisRepository;
import com.waruru.areyouhere.attendance.exception.AlreadyAttendException;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.attendance.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionIdRedisRepository;
import com.waruru.areyouhere.attendance.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO : 일관성있는 메소드명

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceRedisServiceImpl implements AttendanceRedisService {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;
    private final AttendeeRepository attendeeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
    private final AttendanceRedisRepository attendanceRedisRepository;

    @Override
    public List<AttendeeInfo> getNameSakeInfos(String authCode, String attendeeName) {
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        List<AttendeeRedisData> attendees = currentSessionAttendanceInfoData.getAttendees();
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
    public AuthCodeInfo isAttendPossible(String authCode, String attendeeName, Long attendeeId) {

        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        AttendeeRedisData attendeeInfo = getAttendeeInSession(attendeeName, attendeeId,
                currentSessionAttendanceInfoData);

        if (attendanceRedisRepository.isAlreadyAttended(authCode, attendeeInfo)) {
            throw new AlreadyAttendException();
        }

        attendanceRedisRepository.setAttend(authCode, attendeeInfo);

        return AuthCodeInfo.builder()
                .courseName(currentSessionAttendanceInfoData.getCourseName())
                .sessionName(currentSessionAttendanceInfoData.getSessionName())
                .sessionId(currentSessionAttendanceInfoData.getSessionId())
                .build();
    }


    @Override
    public String createAuthCode(Course course, Session session, LocalDateTime currentTime) {
        String generatedAuthCode = generateAuthCode();

        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(course.getId());

        CurrentSessionAttendanceInfo currentSessionAttendanceInfo = CurrentSessionAttendanceInfo.builder()
                .sessionId(session.getId())
                .authCode(generatedAuthCode)
                .attendees(attendeesByCourseId)
                .sessionName(session.getName())
                .courseName(course.getName())
                .build();

        authCodeRedisRepository.save(currentSessionAttendanceInfo);

        SessionId newsessionId = SessionId.builder()
                .authCode(currentSessionAttendanceInfo.getAuthCode())
                .sessionId(session.getId())
                .build();

        sessionIdRedisRepository.save(newsessionId);

        return generatedAuthCode;
    }


    // TODO : sessionId 검증, 해당 sessionId가 user 소유인지 검증.
    @Override
    public void deactivate(String authCode) {
        CurrentSessionAttendanceInfo authCodeByCurrentSessionAttendanceInfo = authCodeRedisRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        authCodeRedisRepository.delete(authCodeByCurrentSessionAttendanceInfo);
        sessionIdRedisRepository.deleteById(authCodeByCurrentSessionAttendanceInfo.getSessionId());
        attendanceRedisRepository.deleteAllAttendanceInSession(authCode);

    }

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendanceInfo(String authCode) {
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = authCodeRedisRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);
        List<AttendeeRedisData> attendees = new LinkedList<>();
        List<AttendeeRedisData> absentees = new LinkedList<>();
        Set<Long> attendeesChecker = attendanceRedisRepository.getAttendees(authCode);
        currentSessionAttendanceInfoData.getAttendees()
                .forEach(att -> {
                    if (attendeesChecker.contains(att.getId())) {
                        attendees.add(att);
                    } else {
                        absentees.add(att);
                    }
                });
        return CurrentSessionAttendeeAttendance.builder()
                .attendees(attendees)
                .absentees(absentees)
                .build();
    }

    private AttendeeRedisData getAttendeeInSession(String attendeeName, Long attendeeId,
                                                   CurrentSessionAttendanceInfo currentSessionAttendanceInfoData) {
        AttendeeRedisData attendeeInfo = currentSessionAttendanceInfoData.getAttendees().stream()
                .filter(att -> att.getName().equals(attendeeName))
                .findAny()
                .orElseThrow(AttendeeNotFoundException::new);

        if (attendeeId != null) {
            attendeeInfo = currentSessionAttendanceInfoData.getAttendees().stream()
                    .filter(att -> att.getId().equals(attendeeId))
                    .findAny()
                    .orElseThrow(AttendeeNotFoundException::new);
        }
        return attendeeInfo;
    }

    private String generateAuthCode() {
        String generatedAuthCode = "";
        while (true) {
            generatedAuthCode = randomIdentifierGenerator.generateRandomIdentifier(4);
            Optional<CurrentSessionAttendanceInfo> authCodeData = authCodeRedisRepository
                    .findById(generatedAuthCode);

            if (authCodeData.isEmpty()) {
                break;
            }
        }
        return generatedAuthCode;
    }


}