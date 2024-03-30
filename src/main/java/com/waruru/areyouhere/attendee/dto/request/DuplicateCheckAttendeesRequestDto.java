package com.waruru.areyouhere.attendee.dto.request;

import java.util.List;
import lombok.Getter;


@Getter
public class DuplicateCheckAttendeesRequestDto {
    private Long courseId;
    private List<String> newAttendees;
}
