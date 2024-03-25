package com.waruru.areyouhere.course.dto.request;

import com.waruru.areyouhere.attendee.dto.AttendeeData;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CourseCreationRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private List<AttendeeData> attendees;

    @NotEmpty
    private boolean onlyListNameAllowed;
}
