package com.waruru.areyouhere.attendee.dto;

import java.util.List;
import lombok.Getter;


@Getter
public class DuplicateCheckAttendeesDto {
    private Long courseId;
    private List<String> newAttendees;
}
