package com.waruru.areyouhere.attendance;

import com.waruru.areyouhere.attendance.dto.AttendRequestDto;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.session.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<HttpStatus> attend(@RequestBody AttendRequestDto attendRequestDto){
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();

        Long sessionId = authCodeService.checkAuthCodeAndGetSessionId(attendeeName, authCode);
        attendanceService.setAttend(sessionId, authCode);
        return ResponseEntity.ok().build();
    }

}
