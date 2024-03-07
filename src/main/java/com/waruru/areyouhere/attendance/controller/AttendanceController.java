package com.waruru.areyouhere.attendance.controller;

import com.waruru.areyouhere.attendance.dto.AttendRequestDto;
import com.waruru.areyouhere.attendance.dto.CurrentAttendanceCount;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.attendance.service.AuthCodeService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttendanceController.ATTENDANCE_API_URL)
public class AttendanceController {
    public static final String ATTENDANCE_API_URL = "/api/attendance";

    private final AuthCodeService authCodeService;
    private final AttendanceService attendanceService;
    private final AttendeeService attendeeService;

    @PostMapping
    public ResponseEntity<HttpStatus> attend(@RequestBody AttendRequestDto attendRequestDto){
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();

        Long sessionId = authCodeService.checkAuthCodeAndGetSessionId(authCode, attendeeName);
        attendanceService.setAttend(sessionId, attendeeName);
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @PutMapping("/update")
    ResponseEntity<HttpStatus> updateAttendances(@RequestBody UpdateAttendanceRequestDto updateAttendanceRequestDto){
        Long sessionId = updateAttendanceRequestDto.getSessionId();
        List<UpdateAttendance> updateAttendance = updateAttendanceRequestDto.getUpdateAttendances();

        attendanceService.setAttendanceStatuses(sessionId, updateAttendance);
        return ResponseEntity.ok().build();
    }


    // TODO : 아예 바꿔
    @LoginRequired
    @GetMapping("{courseId}/{sessionId}")
    ResponseEntity<CurrentAttendanceCount> getCurrentAttendances(@PathVariable("courseId") Long courseId, @PathVariable("sessionId") Long sessionId){
        int currentAttendance = attendanceService.currentAttendance(sessionId);

        int total = attendeeService.getAttendeeByCourseId(courseId);

        return ResponseEntity.ok(new CurrentAttendanceCount(currentAttendance, total));
    }

}
