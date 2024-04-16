package com.waruru.areyouhere.active;


import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendance.dto.request.AuthCodeDeactivationRequestDto;
import com.waruru.areyouhere.attendance.dto.request.AuthCodeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ActiveSessionController.AUTH_CODE_API_URL)
public class ActiveSessionController {

    public static final String AUTH_CODE_API_URL = "/api/auth-code";

    private final AttendanceService attendanceService;


    @PostMapping
    public ResponseEntity<String> activate(@RequestBody AuthCodeRequestDto authCodeRequestDto) {
        Long sessionId = authCodeRequestDto.getSessionId();
        Long courseId = authCodeRequestDto.getCourseId();
        return ResponseEntity.ok(attendanceService.activateSession(sessionId, courseId));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivate(
            @RequestBody AuthCodeDeactivationRequestDto authCodeDeactivationRequestDto) {
        Long sessionId = authCodeDeactivationRequestDto.getSessionId();
        Long courseId = authCodeDeactivationRequestDto.getCourseId();
        String authCode = authCodeDeactivationRequestDto.getAuthCode();
        attendanceService.deactivateSession(authCode, sessionId, courseId);
        return ResponseEntity.ok().build();
    }


}
