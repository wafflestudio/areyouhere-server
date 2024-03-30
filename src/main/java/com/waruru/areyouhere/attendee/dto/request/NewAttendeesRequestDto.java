package com.waruru.areyouhere.attendee.dto.request;

import com.waruru.areyouhere.attendee.service.dto.AttendeeData;
import java.util.List;
import lombok.Data;


@Data
public class NewAttendeesRequestDto {
    private Long courseId;

    List<AttendeeData> newAttendees;
}

