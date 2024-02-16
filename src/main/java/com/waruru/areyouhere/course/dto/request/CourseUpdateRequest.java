package com.waruru.areyouhere.course.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CourseUpdateRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private boolean onlyListNameAllowed;
}
