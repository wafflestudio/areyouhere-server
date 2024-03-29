package com.waruru.areyouhere.attendance.controller;

import com.waruru.areyouhere.attendance.dto.AttendRequestDto;
import com.waruru.areyouhere.attendance.dto.AttendResponseDto;
import com.waruru.areyouhere.attendance.dto.CurrentAttendanceCount;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.exception.DuplicateAuthCodeAttendException;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
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

    //FIXME: 중복 인원인 경우 코드 중복이 생겨도 API 분리가 나아 보인다. 한 API에서 너무 많은 일을 하는 중.
    @PostMapping
    public ResponseEntity<AttendResponseDto> attend(HttpServletRequest request, @RequestBody AttendRequestDto attendRequestDto){
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();
        Long attendeeId = attendRequestDto.getAttendeeId();
        LocalDateTime attendanceTime = LocalDateTime.now();


        List<AttendeeInfo> nameSakeAttendees = authCodeService.hasNameSake(authCode, attendeeName);

        // 동명이인 응답
        if(attendeeId == null && nameSakeAttendees.size() > 1){
            return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES).body(
                    AttendResponseDto.builder()
                            .attendeeNotes(nameSakeAttendees)
                            .build()
            );
        }

        AuthCodeInfo authCodeInfo = authCodeService.isAttendPossible(authCode, attendeeName, attendeeId);
        checkAuthCodeCookie(request.getCookies(), authCode);
        attendanceService.setAttend(authCodeInfo.getSessionId(), attendeeName, attendeeId);



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

    @LoginRequired
    @GetMapping
    ResponseEntity<CurrentAttendanceCount> getCurrentAttendances(@RequestParam("courseId") Long courseId, @RequestParam("sessionId") Long sessionId){
        int currentAttendance = attendanceService.currentAttendance(sessionId);
        int total = attendeeService.getAttendeeByCourseId(courseId);
        return ResponseEntity.ok(new CurrentAttendanceCount(currentAttendance, total));
    }

    @LoginRequired
    @GetMapping("/detail")
    ResponseEntity<CurrentSessionAttendeeAttendance> getCurrentSessionAttendeeAttendance(@RequestParam("authCode") String authCode){
        return ResponseEntity.ok(authCodeService.getCurrentSessionAttendanceInfo(authCode));
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
