package com.waruru.areyouhere.attendee.dto.request;

import com.waruru.areyouhere.attendee.service.dto.AttendeeData;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import java.util.List;
import lombok.Data;


@Data
public class NewAttendeesRequestDto {
    private Long courseId;

    List<AttendeeInfo> newAttendees;
}

