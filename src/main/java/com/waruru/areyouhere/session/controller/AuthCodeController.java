package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.session.dto.AuthCodeDeactivationRequestDto;
import com.waruru.areyouhere.session.dto.AuthCodeRequestDto;
import com.waruru.areyouhere.session.service.AuthCodeService;
import com.waruru.areyouhere.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthCodeController.AUTH_CODE_API_URL)
public class AuthCodeController {

    public static final String AUTH_CODE_API_URL = "/api/auth-code";

    private final AuthCodeService authCodeService;
    private final AttendanceService attendanceService;
    private final SessionService sessionService;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthCodeRequestDto authCodeRequestDto){

        return ResponseEntity.ok(authCodeService.createAuthCode(authCodeRequestDto.getCourseId(), authCodeRequestDto.getSessionId()));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivate(@RequestBody AuthCodeDeactivationRequestDto authCodeDeactivationRequestDto){
        authCodeService.deactivate(authCodeDeactivationRequestDto.getAuthCode());
        sessionService.checkSessionNotDeactivated(authCodeDeactivationRequestDto.getSessionId());
        attendanceService.setAbsentAfterDeactivation(authCodeDeactivationRequestDto.getCourseId(), authCodeDeactivationRequestDto.getSessionId());
        return ResponseEntity.ok().build();
    }


}
