package com.waruru.areyouhere.attendee.dto;

import java.util.List;
import lombok.Data;

@Data
public class DeleteAttendeesDto {
    List<Long> attendeeIds;
}
