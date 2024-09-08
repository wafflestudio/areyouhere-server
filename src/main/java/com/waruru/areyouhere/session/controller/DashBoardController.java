package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.session.dto.response.CurrentSessionResponseDto;
import com.waruru.areyouhere.session.dto.response.PreviousFiveSessionResponseDto;
import com.waruru.areyouhere.session.exception.CurrentSessionNotFoundException;
import com.waruru.areyouhere.session.service.command.SessionCommandService;
import com.waruru.areyouhere.session.service.dto.CurrentSessionDto;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import com.waruru.areyouhere.session.service.query.SessionQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    private final SessionQueryService sessionQueryService;
    private final SessionCommandService sessionCommandService;

    @LoginRequired
    @GetMapping
    public ResponseEntity<CurrentSessionResponseDto> getCurrentSessionInfo(
            @Login Manager manager,
            @PathVariable Long courseId) {
        CurrentSessionDto currentSessionInfo = sessionQueryService.getCurrentSessionInfo(manager.getId(), courseId);
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
    public ResponseEntity<PreviousFiveSessionResponseDto> getRecentFiveSessionInfo(
            @Login Manager manager,
            @PathVariable Long courseId){

        List<SessionAttendanceInfo> recentFiveSessions = sessionQueryService.getRecentFive(manager.getId(), courseId);
        if(recentFiveSessions.isEmpty()){
            throw new CurrentSessionNotFoundException();
        }


        return ResponseEntity.ok(PreviousFiveSessionResponseDto.builder()
                .sessionAttendanceInfos(recentFiveSessions)
                .build());
    }


}
