package com.waruru.areyouhere.attendee.dto;

import java.util.List;
import lombok.Data;


@Data
public class NewAttendeesDto {
    private Long courseId;

    List<String> newAttendees;
}
