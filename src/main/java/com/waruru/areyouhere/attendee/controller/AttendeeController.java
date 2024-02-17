package com.waruru.areyouhere.attendee.controller;

import com.waruru.areyouhere.attendee.dto.ClassAttendeesDto;
import com.waruru.areyouhere.attendee.service.AttendeeService;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttendeeController.ATTENDEE_API_URL)
public class AttendeeController {
    public static final String ATTENDEE_API_URL = "/api/attendee/";

    private final AttendeeService attendeeService;

    @GetMapping("{courseId}")
    public ResponseEntity<ClassAttendeesDto> getClassAttendees(@PathVariable("courseId") Long courseId){
        List<ClassAttendees> classAttendees = attendeeService.getClassAttendeesIfExistsOrEmpty(courseId);
        if(classAttendees.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ClassAttendeesDto.builder()
                .classAttendees(classAttendees)
                .build());
    }
}
