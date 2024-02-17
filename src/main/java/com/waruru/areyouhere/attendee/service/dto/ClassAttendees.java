package com.waruru.areyouhere.attendee.service.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassAttendees {

    private String name;

    private int attendance;

    private int absenece;

    @Builder
    public ClassAttendees(String name, int attendance, int absenece) {
        this.name = name;
        this.attendance = attendance;
        this.absenece = absenece;
    }
}
