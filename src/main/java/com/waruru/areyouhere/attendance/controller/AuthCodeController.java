package com.waruru.areyouhere.attendance.controller;


import com.waruru.areyouhere.attendance.service.rdb.AttendanceRDBService;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.service.CourseService;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.attendance.dto.request.AuthCodeDeactivationRequestDto;
import com.waruru.areyouhere.attendance.dto.request.AuthCodeRequestDto;
import com.waruru.areyouhere.active.ActiveSessionService;
import com.waruru.areyouhere.session.service.command.SessionCommandService;
import com.waruru.areyouhere.session.service.query.SessionQueryService;
import java.time.LocalDateTime;
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

    private final ActiveSessionService activeSessionService;
    private final AttendanceRDBService attendanceRDBService;
    private final SessionCommandService sessionCommandService;
    private final SessionQueryService sessionQueryService;
    private final CourseService courseService;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthCodeRequestDto authCodeRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();
        Long sessionId = authCodeRequestDto.getSessionId();
        Long courseId = authCodeRequestDto.getCourseId();
        Course course = courseService.get(courseId);
        Session session = sessionQueryService.get(sessionId);
        sessionCommandService.setStartTime(sessionId, currentTime);
        return ResponseEntity.ok(activeSessionService.createAuthCode(course, session, currentTime));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivate(
            @RequestBody AuthCodeDeactivationRequestDto authCodeDeactivationRequestDto) {
        Long sessionId = authCodeDeactivationRequestDto.getSessionId();
        Long courseId = authCodeDeactivationRequestDto.getCourseId();

        String authCode = authCodeDeactivationRequestDto.getAuthCode();

        sessionQueryService.checkNotDeactivated(sessionId);
        sessionCommandService.deactivate(sessionId);
        activeSessionService.deactivate(authCode);
        attendanceRDBService.setAbsentAfterDeactivation(courseId, sessionId);
        return ResponseEntity.ok().build();
    }


}
