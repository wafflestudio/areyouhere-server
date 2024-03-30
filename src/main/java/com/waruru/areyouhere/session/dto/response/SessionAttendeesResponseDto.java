package com.waruru.areyouhere.session.dto.response;

import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionAttendeesResponseDto {

    private List<SessionAttendees> sessionAttendees;

    @Builder
    public SessionAttendeesResponseDto(List<SessionAttendees> sessionAttendees) {
        this.sessionAttendees = sessionAttendees;
    }
}
