package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.response.AttendResponseDto;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendCount;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendance.service.rdb.AttendanceRDBService;
import com.waruru.areyouhere.active.service.ActiveAttendanceService;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 출석 로직이 너무 비대해서 facade 패턴으로 상위 레포지토리를 분리했는데 출석 외의 로직이 방대하지 않아서
// TODO: 그 외엔 필요가 없어보인다..
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRDBService attendanceRDBService;
    private final ActiveAttendanceService activeAttendanceService;

    @Override
    @Transactional
    public AttendResponseDto attend(String attendeeName, String authCode, Long attendeeId) {
        LocalDateTime attendanceTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        List<AttendeeInfo> nameSakeAttendees = activeAttendanceService.getNameSakeInfos(authCode, attendeeName);

        // 동명이인 응답
        if (attendeeId == null && nameSakeAttendees.size() > 1) {
            return AttendResponseDto.builder()
                    .attendeeNotes(nameSakeAttendees)
                    .build();
        }

        AuthCodeInfo authCodeInfo = activeAttendanceService.isAttendPossible(authCode, attendeeName, attendeeId);
        AttendeeRedisData attendeeInSession = activeAttendanceService.findByNameIfNotDuplicatedOrId(attendeeName,
                attendeeId,
                activeAttendanceService.getSessionAttendanceInfoOrThrow(authCode));
        activeAttendanceService.setAttendInRedis(authCode, attendeeInSession);

        return AttendResponseDto.builder()
                .attendanceName(attendeeName)
                .courseName(authCodeInfo.getCourseName())
                .sessionName(authCodeInfo.getSessionName())
                .attendanceTime(attendanceTime).build();

    }

    @Override
    @Transactional
    public void updateAllStatuses(Long sessionId, List<UpdateAttendance> updateAttendances) {
        attendanceRDBService.setAttendanceStatuses(sessionId, updateAttendances);
    }

    @Override
    @Transactional(readOnly = true)
    public CurrentSessionAttendCount getCurrentSessionAttendCount(Long sessionId) {
        String authCode = activeAttendanceService.findAuthCodeBySessionId(sessionId);
        int total = activeAttendanceService.getTotalAttendees(authCode);
        int attendeeCount = activeAttendanceService.getAttendCount(authCode);
        return new CurrentSessionAttendCount(total, attendeeCount);
    }

    @Override
    @Transactional(readOnly = true)
    public CurrentSessionAttendeeAttendance getCurrentSessionAttendeesAndAbsentees(String authCode) {
        return activeAttendanceService.getCurrentSessionAttendees(authCode);
    }



}
