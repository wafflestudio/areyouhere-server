package com.waruru.areyouhere.attendee.service.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassAttendees {

    private AttendeeInfo attendeeInfo;

    private int attendance;

    private int absence;

    @Builder
    public ClassAttendees(Long id, String name, String note, int attendance, int absence) {
        this.attendeeInfo = AttendeeInfo.builder()
                .id(id)
                .name(name)
                .note(note)
                .build();
        this.attendance = attendance;
        this.absence = absence;
    }
}
