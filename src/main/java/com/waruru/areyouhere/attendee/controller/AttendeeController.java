package com.waruru.areyouhere.attendee.controller;

import com.waruru.areyouhere.attendee.service.dto.AttendeeDetailDto;
import com.waruru.areyouhere.attendee.dto.response.ClassAttendeesResponseDto;
import com.waruru.areyouhere.attendee.dto.request.DeleteAttendeesRequestDto;
import com.waruru.areyouhere.attendee.dto.request.DuplicateCheckAttendeesRequestDto;
import com.waruru.areyouhere.attendee.dto.request.NewAttendeesRequestDto;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(AttendeeController.ATTENDEE_API_URL)
public class AttendeeController {
    public static final String ATTENDEE_API_URL = "/api/attendee";

    private final AttendeeService attendeeService;

    @LoginRequired
    @GetMapping
    public ResponseEntity<ClassAttendeesResponseDto> getClassAttendees(@RequestParam("courseId") Long courseId){
        List<ClassAttendees> classAttendees = attendeeService.getClassAttendeesIfExistsOrEmpty(courseId);

        return ResponseEntity.ok(ClassAttendeesResponseDto.builder()
                .classAttendees(classAttendees)
                .build());
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody NewAttendeesRequestDto newAttendeesRequestDto){
        attendeeService.createAttendees(newAttendeesRequestDto.getCourseId(), newAttendeesRequestDto.getNewAttendees());
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @PostMapping("/delete")
    public ResponseEntity<HttpStatus> delete(@RequestBody DeleteAttendeesRequestDto deleteAttendeesRequestDto){
        attendeeService.deleteAttendees(deleteAttendeesRequestDto.getAttendeeIds());
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @PostMapping("/duplicate")
    public ResponseEntity<DuplicateAttendees> checkDuplicate(@RequestBody DuplicateCheckAttendeesRequestDto duplicateCheckAttendeesRequestDto){
        return ResponseEntity.ok(attendeeService.getDuplicateAttendees(duplicateCheckAttendeesRequestDto.getCourseId(), duplicateCheckAttendeesRequestDto.getNewAttendees()));
    }

    @LoginRequired
    @GetMapping("/detail")
    public ResponseEntity<AttendeeDetailDto> getAttendeeDetail(@RequestParam("attendeeId") Long attendeeId){
        return ResponseEntity.ok(attendeeService.getAttendeeDetail(attendeeId));
    }
}

