package com.waruru.areyouhere.attendee.dto.response;

import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassAttendeesResponseDto {

    private List<ClassAttendees> classAttendees;

    @Builder
    public ClassAttendeesResponseDto(List<ClassAttendees> classAttendees) {
        this.classAttendees = classAttendees;
    }
}
