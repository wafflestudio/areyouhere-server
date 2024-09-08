package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.attendee.service.query.AttendeeQueryService;
import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.session.dto.request.CreateSessionRequestDto;
import com.waruru.areyouhere.session.dto.request.DeleteSessionRequestDto;
import com.waruru.areyouhere.session.dto.request.UpdateSessionsRequestDto;
import com.waruru.areyouhere.session.dto.response.SessionAttendeesResponseDto;
import com.waruru.areyouhere.session.service.command.SessionCommandService;
import com.waruru.areyouhere.session.service.dto.AllSessionAttendanceInfo;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import com.waruru.areyouhere.session.service.query.SessionQueryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(SessionController.SESSION_API_URL)
public class SessionController {

    public static final String SESSION_API_URL = "/api/session";
    
    private final SessionCommandService sessionCommandService;
    private final SessionQueryService sessionQueryService;
    private final AttendeeQueryService attendeeQueryService;

    // TODO : refactor => service Dto 그대로 사용.
    @LoginRequired
    @GetMapping
    public ResponseEntity<AllSessionAttendanceInfo> getAll(
            @Login Manager manager,
            @RequestParam("courseId") Long courseId){

        List<SessionAttendanceInfo> allSessions = sessionQueryService.getAll(manager.getId(), courseId);
        if(allSessions.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(AllSessionAttendanceInfo.builder()
                .allSessionAttendanceInfo(allSessions)
                .build());
    }
    @LoginRequired
    @PostMapping
    public ResponseEntity<HttpStatus> create(
            @Login Manager manager,
            @RequestBody CreateSessionRequestDto createSessionRequestDto){
        sessionCommandService.create(manager.getId(), createSessionRequestDto.getCourseId(), createSessionRequestDto.getSessionName());
        return ResponseEntity.ok().build();
    }
    @LoginRequired
    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> delete(
            @Login Manager manager,
            @RequestBody DeleteSessionRequestDto sessionIds){
        sessionCommandService.deleteAll(manager.getId(), sessionIds.getSessionIds());
        return ResponseEntity.ok().build();
    }
    @LoginRequired
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteNotActivated(
            @Login Manager manager,
            @RequestParam("courseId") Long courseId){
        sessionCommandService.deleteNotActivated(manager.getId(), courseId);
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionAttendanceInfo> getSessionBasicInfo(
            @Login Manager manager,
            @PathVariable("sessionId") Long sessionId){
        return ResponseEntity.ok(sessionQueryService.getSessionAttendanceInfo(manager.getId(), sessionId));
    }
    // TODO: refactor => attendeeService로 이동
    @LoginRequired
    @GetMapping("/{sessionId}/attendee")
    public ResponseEntity<SessionAttendeesResponseDto> getSessionAllAttendees(@PathVariable("sessionId") Long sessionId){
        List<SessionAttendees> sessionAttendees = attendeeQueryService.getSessionAttendeesIfExistsOrEmpty(
                sessionId);

        return ResponseEntity.ok(SessionAttendeesResponseDto.builder()
                .sessionAttendees(sessionAttendees)
                .build());
    }
    @LoginRequired
    @GetMapping("/{sessionId}/absentee")
    public ResponseEntity<SessionAttendeesResponseDto> getSessionAbsenteeOnly(@PathVariable("sessionId") Long sessionId){
        List<SessionAttendees> sessionAttendees = attendeeQueryService.getSessionAbsenteesIfExistsOrEmpty(
                sessionId);

        return ResponseEntity.ok(SessionAttendeesResponseDto.builder()
                .sessionAttendees(sessionAttendees)
                .build());
    }
    @LoginRequired
    @PutMapping
    public ResponseEntity<HttpStatus> updateAll(
            @Login Manager manager,
            @RequestBody UpdateSessionsRequestDto updateSessionsRequestDto){
        sessionCommandService.updateAll(manager.getId(), updateSessionsRequestDto.getSessions());
        return ResponseEntity.ok().build();
    }

}
