package com.waruru.areyouhere.attendee.dto;

import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassAttendeesDto {

    private List<ClassAttendees> classAttendees;

    @Builder
    public ClassAttendeesDto(List<ClassAttendees> classAttendees) {
        this.classAttendees = classAttendees;
    }
}
