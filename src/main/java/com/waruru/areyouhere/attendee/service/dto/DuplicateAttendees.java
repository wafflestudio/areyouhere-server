package com.waruru.areyouhere.attendee.service.dto;

import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class DuplicateAttendees {
    public final List<AttendeeInfo> duplicatedAttendees;

    public void addDuplicateAttendee(Long id, String name, String note) {
        duplicatedAttendees.add(AttendeeInfo.builder()
                .id(id)
                .name(name)
                .note(note)
                .build());
    }
}
