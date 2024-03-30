package com.waruru.areyouhere.attendee.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class DeleteAttendeesRequestDto {
    List<Long> attendeeIds;
}
