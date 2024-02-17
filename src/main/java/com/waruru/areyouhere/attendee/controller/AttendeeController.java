package com.waruru.areyouhere.attendee.controller;

import com.waruru.areyouhere.attendee.dto.ClassAttendeesDto;
import com.waruru.areyouhere.attendee.dto.DeleteAttendeesDto;
import com.waruru.areyouhere.attendee.dto.NewAttendeesDto;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttendeeController.ATTENDEE_API_URL)
public class AttendeeController {
    public static final String ATTENDEE_API_URL = "/api/attendee";

    private final AttendeeService attendeeService;

    @GetMapping("/{courseId}")
    public ResponseEntity<ClassAttendeesDto> getClassAttendees(@PathVariable("courseId") Long courseId){
        List<ClassAttendees> classAttendees = attendeeService.getClassAttendeesIfExistsOrEmpty(courseId);
        if(classAttendees.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ClassAttendeesDto.builder()
                .classAttendees(classAttendees)
                .build());
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody NewAttendeesDto newAttendeesDto){
        attendeeService.createAttendees(newAttendeesDto.getCourseId(), newAttendeesDto.getNewAttendees());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@RequestBody DeleteAttendeesDto deleteAttendeesDto){
        attendeeService.deleteAttendees(deleteAttendeesDto.getAttendeeIds());
        return ResponseEntity.ok().build();
    }
}
