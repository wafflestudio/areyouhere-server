package com.waruru.areyouhere.active.service;

import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.active.domain.repository.ActiveSessionRepository;
import com.waruru.areyouhere.attendance.exception.AuthCodeNotFoundException;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.attendance.exception.AlreadyAttendException;
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
public class ActiveAttendanceServiceImpl implements ActiveAttendanceService {

    private final ActiveSessionRepository activeSessionRepository;
    private final AttendeeRepository attendeeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;


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

        if (currentSessionAttendanceInfoData.isAlreadyAttended(attendeeId)) {
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
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = getSessionAttendanceInfoOrThrow(
                authCode);
        currentSessionAttendanceInfoData.setAttendanceTime(attendeeInfo.getId(), LocalDateTime.now());
        activeSessionRepository.save(currentSessionAttendanceInfoData);
    }


    @Override
    public String activate(Course course, Session session, LocalDateTime currentTime) {
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

        activeSessionRepository.save(currentSessionAttendanceInfo);

        return generatedAuthCode;
    }


    // TODO : sessionId 검증, 해당 sessionId가 user 소유인지 검증.
    @Override
    public void deactivate(String authCode) {
        CurrentSessionAttendanceInfo authCodeByCurrentSessionAttendanceInfo = getSessionAttendanceInfoOrThrow(
                authCode);

        activeSessionRepository.delete(authCodeByCurrentSessionAttendanceInfo);
    }

    public CurrentSessionAttendeeAttendance getCurrentSessionAttendees(String authCode) {
        CurrentSessionAttendanceInfo currentSessionAttendanceInfoData = getSessionAttendanceInfoOrThrow(
                authCode);
        List<AttendeeRedisData> attendees = new LinkedList<>();
        List<AttendeeRedisData> absentees = new LinkedList<>();
        Set<Long> attendeesChecker = currentSessionAttendanceInfoData.getAttendAttendeesIds();
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
        return activeSessionRepository.findBySessionId(sessionId)
                .orElseThrow(AuthCodeNotFoundException::new).getAuthCode();
    }


    public int getTotalAttendees(String authCode) {
        return activeSessionRepository.findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new).getAttendees().size();
    }

    public void updateCourseName(Long courseId, String courseName) {
        getCurrentSessionAttendanceInfoByCourseId(courseId)
                .ifPresent(currentSessionAttendanceInfoData -> {
                    currentSessionAttendanceInfoData.setCourseName(courseName);
                    activeSessionRepository.save(currentSessionAttendanceInfoData);
                });
    }

    public void updateAttendees(Long courseId, List<Attendee> attendees) {
        getCurrentSessionAttendanceInfoByCourseId(courseId)
                .ifPresent(currentSessionAttendanceInfoData -> {
                    currentSessionAttendanceInfoData.updateAttendees(attendees);
                    activeSessionRepository.save(currentSessionAttendanceInfoData);
                });
    }


    public Optional<CurrentSessionAttendanceInfo> getCurrentSessionAttendanceInfoByCourseId(Long courseId) {
        return activeSessionRepository.findByCourseId(courseId);
    }

    public CurrentSessionAttendanceInfo getSessionAttendanceInfoOrThrow(String authCode) {
        return activeSessionRepository
                .findById(authCode)
                .orElseThrow(AuthCodeNotFoundException::new);
    }


    public int getAttendCount(String authCode) {
        return getSessionAttendanceInfoOrThrow(authCode).getAttendAttendeesIds().size();
    }

    public boolean isSessionActivatedByCourseId(Long courseId) {
        return activeSessionRepository.findByCourseId(courseId).isPresent();
    }


    private String generateAuthCode() {
        String generatedAuthCode = "";
        while (true) {
            generatedAuthCode = randomIdentifierGenerator.generateRandomIdentifier(4);
            Optional<CurrentSessionAttendanceInfo> authCodeData = activeSessionRepository
                    .findById(generatedAuthCode);

            if (authCodeData.isEmpty()) {
                break;
            }
        }
        return generatedAuthCode;
    }


}
