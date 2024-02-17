package com.waruru.areyouhere.attendee.service.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassAttendees {

    private Long id;

    private String name;

    private int attendance;

    private int absenece;

    @Builder
    public ClassAttendees(Long id, String name, int attendance, int absenece) {
        this.id = id;
        this.name = name;
        this.attendance = attendance;
        this.absenece = absenece;
    }
}
