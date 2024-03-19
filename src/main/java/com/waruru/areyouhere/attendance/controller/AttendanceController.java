package com.waruru.areyouhere.attendance.controller;

import com.waruru.areyouhere.attendance.dto.AttendRequestDto;
import com.waruru.areyouhere.attendance.dto.AttendResponseDto;
import com.waruru.areyouhere.attendance.dto.CurrentAttendanceCount;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.exception.DuplicateAuthCodeAttendException;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.attendance.service.AuthCodeService;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttendanceController.ATTENDANCE_API_URL)
public class AttendanceController {
    public static final String ATTENDANCE_API_URL = "/api/attendance";

    private final AuthCodeService authCodeService;
    private final AttendanceService attendanceService;
    private final AttendeeService attendeeService;

    private final String COOKIE_ENCODE = "SRCT";

    @PostMapping
    public ResponseEntity<AttendResponseDto> attend(HttpServletRequest request, @RequestBody AttendRequestDto attendRequestDto){
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();
        LocalDateTime attendanceTime = LocalDateTime.now();

        checkAuthCodeCookie(request.getCookies(), authCode);

        AuthCodeInfo authCodeInfo = authCodeService.checkAuthCodeAndGetSessionId(authCode, attendeeName);
        attendanceService.setAttend(authCodeInfo.getSessionId(), attendeeName);

        HttpCookie authCodeCookie = getAuthCodeCookie(authCode);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCodeCookie.toString())
                .body(
                    AttendResponseDto.builder()
                    .attendanceName(attendeeName)
                    .courseName(authCodeInfo.getCourseName())
                    .sessionName(authCodeInfo.getSessionName())
                    .attendanceTime(attendanceTime).build()
                );
    }

    @LoginRequired
    @PutMapping
    ResponseEntity<HttpStatus> updateAttendances(@RequestBody UpdateAttendanceRequestDto updateAttendanceRequestDto){
        Long sessionId = updateAttendanceRequestDto.getSessionId();
        List<UpdateAttendance> updateAttendance = updateAttendanceRequestDto.getUpdateAttendances();

        attendanceService.setAttendanceStatuses(sessionId, updateAttendance);
        return ResponseEntity.ok().build();
    }


    // TODO : 아예 바꿔
    @LoginRequired
    @GetMapping
    ResponseEntity<CurrentAttendanceCount> getCurrentAttendances(@RequestParam("courseId") Long courseId, @RequestParam("sessionId") Long sessionId){
        int currentAttendance = attendanceService.currentAttendance(sessionId);
        int total = attendeeService.getAttendeeByCourseId(courseId);
        return ResponseEntity.ok(new CurrentAttendanceCount(currentAttendance, total));
    }

    private String encodeCookieValue(String string){
        return Base64.getEncoder().encodeToString((string).getBytes());
    }

    private String encodeCookieKey(String string){
        StringBuilder stringBuilder = new StringBuilder();

        for(char c : string.toCharArray()){
            stringBuilder.append((c ^ COOKIE_ENCODE.charAt(0)));
        }
        return stringBuilder.toString();
    }

    private HttpCookie getAuthCodeCookie(String authCode){
        return ResponseCookie.from(encodeCookieKey(authCode), encodeCookieValue(authCode))
                .maxAge(60 * 30)
                .httpOnly(true)
                .secure(true)
                .build();
    }

    private void checkAuthCodeCookie(Cookie[] cookies, String authCode){
        Optional<Cookie> authCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(encodeCookieKey(authCode)))
                .findFirst();

        authCookie.ifPresent(cookie -> {
            if(new String(Base64.getDecoder().decode(cookie.getValue())).equals(authCode))
                throw new DuplicateAuthCodeAttendException();
        });
    }


}
