package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.session.dto.CurrentSessionResponseDto;
import com.waruru.areyouhere.session.dto.PreviousFiveSessionResponseDto;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.service.SessionService;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;
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

    public static final String DASHBOARD = "/api/course/{courseId}/dashboard";

    private final SessionService sessionService;

    @LoginRequired
    @GetMapping
    public ResponseEntity<CurrentSessionResponseDto> getCurrentSessionInfo(@PathVariable Long courseId) {
        CurrentSessionDto currentSessionInfo = sessionService.getCurrentSessionInfo(courseId);
        CurrentSessionResponseDto currentSessionResponseDto = CurrentSessionResponseDto.builder()
                .authCode(currentSessionInfo.getAuthCode())
                .sessionName(currentSessionInfo.getSessionName())
                .sessionTime(currentSessionInfo.getSessionTime())
                .id(currentSessionInfo.getId())
                .build();
        return ResponseEntity.ok(currentSessionResponseDto);
    }


    @LoginRequired
    @GetMapping("/session")
    public ResponseEntity<PreviousFiveSessionResponseDto> getRecentFiveSessionInfo(@PathVariable Long courseId){

        List<SessionAttendanceInfo> recentFiveSessions = sessionService.getRecentFiveSessions(courseId);
        if(recentFiveSessions.isEmpty()){
            throw new CurrentSessionNotFoundException();
        }


        return ResponseEntity.ok(PreviousFiveSessionResponseDto.builder()
                .sessionAttendanceInfos(recentFiveSessions)
                .build());
    }


}
