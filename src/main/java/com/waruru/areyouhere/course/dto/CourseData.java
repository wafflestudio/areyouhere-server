package com.waruru.areyouhere.course.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseData {
    private Long id;
    private String name;

    private String description;

    private Boolean allowOnlyRegistered;
    @Builder
    public CourseData(Long id, String name, String description, Boolean allowOnlyRegistered) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.allowOnlyRegistered = allowOnlyRegistered;
    }
}
