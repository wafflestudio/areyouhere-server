package com.waruru.areyouhere.attendance.controller;

import com.waruru.areyouhere.attendance.dto.AttendRequestDto;
import com.waruru.areyouhere.attendance.dto.AttendResponseDto;
import com.waruru.areyouhere.attendance.dto.CurrentAttendanceCount;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.attendance.service.AuthCodeService;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttendanceController.ATTENDANCE_API_URL)
public class AttendanceController {
    public static final String ATTENDANCE_API_URL = "/api/attendance";

    private final AuthCodeService authCodeService;
    private final AttendanceService attendanceService;
    private final AttendeeService attendeeService;


    // FIXME 로직 문제. -> 출석 중복 체크.
    @PostMapping
    public ResponseEntity<AttendResponseDto> attend(@RequestBody AttendRequestDto attendRequestDto){
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();
        LocalDateTime attendanceTime = LocalDateTime.now();
        AuthCodeInfo authCodeInfo = authCodeService.checkAuthCodeAndGetSessionId(authCode, attendeeName);
        attendanceService.setAttend(authCodeInfo.getSessionId(), attendeeName);

        return ResponseEntity.ok(
                AttendResponseDto.builder()
                        .attendanceName(attendeeName)
                        .courseName(authCodeInfo.getCourseName())
                        .sessionName(authCodeInfo.getSessionName())
                        .attendanceTime(attendanceTime).build()
        );
    }

    @LoginRequired
    @PutMapping
    ResponseEntity<HttpStatus> updateAttendances(@RequestBody UpdateAttendanceRequestDto updateAttendanceRequestDto){
        Long sessionId = updateAttendanceRequestDto.getSessionId();
        List<UpdateAttendance> updateAttendance = updateAttendanceRequestDto.getUpdateAttendances();

        attendanceService.setAttendanceStatuses(sessionId, updateAttendance);
        return ResponseEntity.ok().build();
    }


    // TODO : 아예 바꿔
    @LoginRequired
    @GetMapping
    ResponseEntity<CurrentAttendanceCount> getCurrentAttendances(@RequestParam("courseId") Long courseId, @RequestParam("sessionId") Long sessionId){
        int currentAttendance = attendanceService.currentAttendance(sessionId);
        int total = attendeeService.getAttendeeByCourseId(courseId);
        return ResponseEntity.ok(new CurrentAttendanceCount(currentAttendance, total));
    }

}
