package com.waruru.areyouhere.attendance.controller;

import com.waruru.areyouhere.attendance.dto.request.AttendRequestDto;
import com.waruru.areyouhere.attendance.dto.response.AttendResponseDto;
import com.waruru.areyouhere.attendance.dto.response.CurrentAttendanceCountResponseDto;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.request.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.exception.DuplicateAuthCodeAttendException;
import com.waruru.areyouhere.attendance.service.AttendanceService;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendCount;
import com.waruru.areyouhere.attendance.service.dto.CurrentSessionAttendeeAttendance;
import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${cookie.encode}")
    private String COOKIE_ENCODE;

    private final AttendanceService attendanceService;


    // FIXME: 1. 중복 인원인 경우 코드 중복이 생겨도 API 분리가 나아 보인다. 한 API에서 너무 많은 일을 하는 중이다.
    // FIXME: 2. 사실상 다른 형태의 response를 억지로 하나의 controller에서 반환한다고 봐도 무방하다. -> 프론트와 합의 후 변경.
    @PostMapping
    public ResponseEntity<AttendResponseDto> attend(HttpServletRequest request,
                                                    @RequestBody AttendRequestDto attendRequestDto) {
        String attendeeName = attendRequestDto.getAttendeeName();
        String authCode = attendRequestDto.getAuthCode();
        Long attendeeId = attendRequestDto.getAttendeeId();

        checkAuthCodeCookie(request.getCookies(), authCode);
        AttendResponseDto attendResponseDto = attendanceService.attend(attendeeName, authCode, attendeeId);

        HttpCookie authCodeCookie = getAuthCodeCookie(authCode);
        // FIXME: 3. 그러다보니 도메인 영역의 코드를 이해해야 생기는 이런 나쁜 코드가 생긴다.
        return attendResponseDto.getAttendanceName() == null ?
                ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES)
                        .body(attendResponseDto)
                : ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, authCodeCookie.toString())
                        .body(attendResponseDto);

    }

    @LoginRequired
    @PutMapping
    ResponseEntity<HttpStatus> update(
            @Login Manager manager,
            @RequestBody UpdateAttendanceRequestDto updateAttendanceRequestDto) {
        List<UpdateAttendance> updateAttendance = updateAttendanceRequestDto.getUpdateAttendances();
        attendanceService.updateAllStatuses(manager.getId(), updateAttendance);
        return ResponseEntity.ok().build();
    }

    //FIXME: 프론트와 합의하여 Redis data는 AuthCode로 일관되게 받아올 수 있도록 하는 것이 좋을 것 같다.
    @LoginRequired
    @GetMapping
    ResponseEntity<CurrentAttendanceCountResponseDto> getCurrentAttendancesCount(
            @RequestParam("courseId") Long courseId, @RequestParam("sessionId") Long sessionId) {
        CurrentSessionAttendCount currentSessionAttendCount = attendanceService.getCurrentSessionAttendCount(sessionId);
        return ResponseEntity.ok().body(
                new CurrentAttendanceCountResponseDto(
                        currentSessionAttendCount.getTotal(),
                        currentSessionAttendCount.getAttendanceCount()
                )
        );
    }

    @LoginRequired
    @GetMapping("/detail")
    ResponseEntity<CurrentSessionAttendeeAttendance> getCurrentSessionAttendeeAttendance(
            @RequestParam("authCode") String authCode) {
        return ResponseEntity.ok(attendanceService.getCurrentSessionAttendeesAndAbsentees(authCode));
    }


    private String encodeCookieValue(String string) {
        return Base64.getEncoder().encodeToString((string).getBytes());
    }

    private String encodeCookieKey(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : string.toCharArray()) {
            stringBuilder.append((c ^ COOKIE_ENCODE.charAt(0)));
        }
        return stringBuilder.toString();
    }

    private HttpCookie getAuthCodeCookie(String authCode) {
        return ResponseCookie.from(encodeCookieKey(authCode), encodeCookieValue(authCode))
                .maxAge(60 * 30)
                .httpOnly(true)
                .build();
    }

    private void checkAuthCodeCookie(Cookie[] cookies, String authCode) {
        if (cookies == null) {
            return;
        }
        Optional<Cookie> authCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(encodeCookieKey(authCode)))
                .findFirst();

        authCookie.ifPresent(cookie -> {
            if (new String(Base64.getDecoder().decode(cookie.getValue())).equals(authCode)) {
                throw new DuplicateAuthCodeAttendException();
            }
        });
    }

}
