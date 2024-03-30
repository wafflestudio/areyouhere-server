package com.waruru.areyouhere.attendee.dto.request;

import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateAttendeesRequestDto {
    private Long courseId;
    private List<AttendeeInfo> updatedAttendees;
}
