package com.waruru.areyouhere.active;


import com.waruru.areyouhere.attendance.dto.request.AuthCodeDeactivationRequestDto;
import com.waruru.areyouhere.attendance.dto.request.AuthCodeRequestDto;
import com.waruru.areyouhere.active.service.ActiveSessionService;
import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
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

    private final ActiveSessionService activeSessionService;


    @PostMapping
    @LoginRequired
    public ResponseEntity<String> activate(
            @Login Manager manager,
            @RequestBody AuthCodeRequestDto authCodeRequestDto) {
        Long sessionId = authCodeRequestDto.getSessionId();
        Long courseId = authCodeRequestDto.getCourseId();
        return ResponseEntity.ok(activeSessionService.activate(manager.getId(), sessionId, courseId));
    }

    @PostMapping("/deactivate")
    @LoginRequired
    public ResponseEntity<HttpStatus> deactivate(
            @Login Manager manager,
            @RequestBody AuthCodeDeactivationRequestDto authCodeDeactivationRequestDto) {
        Long sessionId = authCodeDeactivationRequestDto.getSessionId();
        Long courseId = authCodeDeactivationRequestDto.getCourseId();
        String authCode = authCodeDeactivationRequestDto.getAuthCode();
        activeSessionService.deactivate(authCode, sessionId, courseId);
        return ResponseEntity.ok().build();
    }


}
