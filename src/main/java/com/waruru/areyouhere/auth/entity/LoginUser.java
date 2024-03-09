package com.waruru.areyouhere.auth.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUser {
    private Long id;
    private Set<Long> courseIds;

    public void addCourseId(Long courseId) {
        courseIds.add(courseId);
    }

    public void removeCourseId(Long courseId) {
        courseIds.remove(courseId);
    }

}
