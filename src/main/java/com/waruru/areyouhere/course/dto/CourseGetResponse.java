package com.waruru.areyouhere.course.dto;

import com.waruru.areyouhere.course.domain.entity.Course;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseGetResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean allowOnlyRegistered;

    @Builder
    public CourseGetResponse(Long id, String name, String description, Boolean allowOnlyRegistered) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.allowOnlyRegistered = allowOnlyRegistered;
    }

    public static CourseGetResponse from(Course course) {
        return new CourseGetResponse(course.getId(), course.getName(), course.getDescription(), course.getAllowOnlyRegistered());
    }
}
