package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.session.dto.CreateSessionRequestDto;
import com.waruru.areyouhere.session.dto.DeleteSessionRequestDto;
import com.waruru.areyouhere.session.dto.SessionAttendeesDto;
import com.waruru.areyouhere.session.service.SessionService;
import com.waruru.areyouhere.session.service.dto.AllSessionAttendanceInfo;
import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SessionController.SESSION_API_URL)
public class SessionController {

    public static final String SESSION_API_URL = "/api/session";

    private final SessionService sessionService;
    private final AttendeeService attendeeService;

    // TODO : refactor => service Dto 그대로 사용.
    @GetMapping
    public ResponseEntity<AllSessionAttendanceInfo> getAllSession(@RequestParam("courseId") Long courseId){

        List<SessionAttendanceInfo> allSessions = sessionService.getAllSessions(courseId);
        if(allSessions.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(AllSessionAttendanceInfo.builder()
                .allSessionAttendanceInfo(allSessions)
                .build());
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody CreateSessionRequestDto createSessionRequestDto){
        sessionService.create(createSessionRequestDto.getCourseId(), createSessionRequestDto.getSessionName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@RequestParam("sessionId") Long sessionId){
        sessionService.delete(sessionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<SessionAttendanceInfo> getSessionBasicInfo(@PathVariable("sessionId") Long sessionId){
        return ResponseEntity.ok(sessionService.getSessionInfo(sessionId));
    }

    @GetMapping("/{sessionId}/attendee")
    public ResponseEntity<SessionAttendeesDto> getSessionAllAttendees(@PathVariable("sessionId") Long sessionId){
        List<SessionAttendees> sessionAttendees = attendeeService.getSessionAttendeesIfExistsOrEmpty(
                sessionId);

        return ResponseEntity.ok(SessionAttendeesDto.builder()
                .sessionAttendees(sessionAttendees)
                .build());
    }

    @GetMapping("/{sessionId}/absentee")
    public ResponseEntity<SessionAttendeesDto> getSessionAbsenteeOnly(@PathVariable("sessionId") Long sessionId){
        List<SessionAttendees> sessionAttendees = attendeeService.getSessionAbsenteesIfExistsOrEmpty(
                sessionId);

        return ResponseEntity.ok(SessionAttendeesDto.builder()
                .sessionAttendees(sessionAttendees)
                .build());
    }

}
