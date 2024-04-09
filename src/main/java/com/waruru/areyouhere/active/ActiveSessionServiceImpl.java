package com.waruru.areyouhere.active;

import com.waruru.areyouhere.active.domain.entity.CourseId;
import com.waruru.areyouhere.active.domain.repository.CourseIdRedisRepository;
import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.active.domain.repository.AttendanceRedisRepository;
import com.waruru.areyouhere.attendance.exception.AlreadyAttendException;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.active.domain.entity.SessionId;
import com.waruru.areyouhere.active.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.active.domain.repository.SessionIdRedisRepository;
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

//TODO : 일관성있는 메소드명

@Service
@RequiredArgsConstructor
public class ActiveSessionServiceImpl implements ActiveSessionService {

    private final AuthCodeRedisRepository authCodeRedisRepository;
    private final SessionIdRedisRepository sessionIdRedisRepository;
    private final AttendanceRedisRepository attendanceRedisRepository;
    private final AttendeeRepository attendeeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
    private final CourseIdRedisRepository courseIdRedisRepository;


    @Override
    public List<AttendeeInfo> getNameSakeInfos(String authCode, String attendeeName) {
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = getSessionAttendanceInfoOrThrow(
                authCode);

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

        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = getSessionAttendanceInfoOrThrow(
                authCode);

        AttendeeRedisData attendeeInfo = findByNameIfNotDuplicatedOrId(attendeeName, attendeeId,
                currentSessionAttendanceInfoData);

        if (attendanceRedisRepository.isAlreadyAttended(authCode, attendeeInfo)) {
            throw new AlreadyAttendException();
        }

        return AuthCodeInfo.builder()
                .courseName(currentSessionAttendanceInfoData.getCourseName())
                .sessionName(currentSessionAttendanceInfoData.getSessionName())
                .sessionId(currentSessionAttendanceInfoData.getSessionId())
                .build();
    }


    @Override
    public void setAttendInRedis(String authCode, AttendeeRedisData attendeeInfo) {
        attendanceRedisRepository.setAttend(authCode, attendeeInfo);
    }


    @Override
    public String createAuthCode(Course course, Session session, LocalDateTime currentTime) {
        String generatedAuthCode = generateAuthCode();

        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(course.getId());

        CurrentSessionAttendanceInfo currentSessionAttendanceInfo = CurrentSessionAttendanceInfo.builder()
                .sessionId(session.getId())
                .courseId(course.getId())
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

        CourseId courseId = CourseId.builder()
                .authCode(currentSessionAttendanceInfo.getAuthCode())
                .courseId(course.getId())
                .build();

        courseIdRedisRepository.save(courseId);

        return generatedAuthCode;
    }


    // TODO : sessionId 검증, 해당 sessionId가 user 소유인지 검증.
    @Override
    public void deactivate(String authCode) {
        CurrentSessionAttendanceInfo authCodeByCurrentSessionAttendanceInfo = getSessionAttendanceInfoOrThrow(
                authCode);

        authCodeRedisRepository.delete(authCodeByCurrentSessionAttendanceInfo);
        courseIdRedisRepository.deleteById(authCodeByCurrentSessionAttendanceInfo.getCourseId());
        sessionIdRedisRepository.deleteById(authCodeByCurrentSessionAttendanceInfo.getSessionId());
        attendanceRedisRepository.deleteAllAttendanceInSession(authCodeByCurrentSessionAttendanceInfo.getAuthCode());
    }

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendees(String authCode) {
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = getSessionAttendanceInfoOrThrow(
                authCode);
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

    @Override
    public AttendeeRedisData findByNameIfNotDuplicatedOrId(String attendeeName, Long attendeeId,
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

    public String findAuthCodeBySessionId(Long sessionId) {
        return sessionIdRedisRepository.findById(sessionId)
                .orElseThrow(AuthCodeNotFoundException::new).getAuthCode();
    }


    public int getTotalAttendees(String authCode) {
        return authCodeRedisRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new).getAttendees().size();
    }

    public void updateCourseName(Long courseId, String courseName) {
        getCurrentSessionAttendanceInfoByCourseId(courseId)
                .ifPresent(currentSessionAttendanceInfoData -> {
                    currentSessionAttendanceInfoData.setCourseName(courseName);
                    authCodeRedisRepository.save(currentSessionAttendanceInfoData);
                });
    }

    public void updateSessionName(Long courseId, String sessionName) {
        getCurrentSessionAttendanceInfoByCourseId(courseId)
                .ifPresent(currentSessionAttendanceInfoData -> {
                    currentSessionAttendanceInfoData.setSessionName(sessionName);
                    authCodeRedisRepository.save(currentSessionAttendanceInfoData);
                });
    }

    public void updateAttendees(Long courseId, List<Attendee> attendees) {
        getCurrentSessionAttendanceInfoByCourseId(courseId)
                .ifPresent(currentSessionAttendanceInfoData -> {
                    currentSessionAttendanceInfoData.updateAttendees(attendees);
                    authCodeRedisRepository.save(currentSessionAttendanceInfoData);
                });
    }


    public Optional<CurrentSessionAttendanceInfo> getCurrentSessionAttendanceInfoByCourseId(Long courseId) {
        return courseIdRedisRepository.findById(courseId)
                .flatMap(findCourseId -> authCodeRedisRepository.findById(findCourseId.getAuthCode()));
    }

    public CurrentSessionAttendanceInfo getSessionAttendanceInfoOrThrow(String authCode) {
        return authCodeRedisRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);
    }


    public int getAttendCount(String authCode) {
        return attendanceRedisRepository.getAttendees(authCode).size();
    }

    public boolean isSessionActivatedByCourseId(Long courseId){
        return courseIdRedisRepository.findById(courseId).isPresent();
    }

    public boolean isSessionActivatedBySessionId(Long sessionId){
        return sessionIdRedisRepository.findById(sessionId).isPresent();
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
