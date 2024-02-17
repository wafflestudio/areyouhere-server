package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.session.dto.CurrentSessionResponseDto;
import com.waruru.areyouhere.session.service.SessionService;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(DashBoardController.DASHBOARD)
public class DashBoardController {

    public static final String DASHBOARD = "/api/course/dashboard";

    private final SessionService sessionService;

    @LoginRequired
    @GetMapping("/{courseId}")
    public ResponseEntity<CurrentSessionResponseDto> getCurrentSessionInfo(@PathVariable Long courseId) {
        CurrentSessionDto currentSessionInfo = sessionService.getCurrentSessionInfo(courseId);
        CurrentSessionResponseDto currentSessionResponseDto = CurrentSessionResponseDto.builder()
                .authCode(currentSessionInfo.getAuthCode())
                .sessionName(currentSessionInfo.getSessionName())
                .sessionTime(currentSessionInfo.getSessionTime())
                .build();
        return ResponseEntity.ok(currentSessionResponseDto);
    }



}
