package com.waruru.areyouhere.course.service;

import com.waruru.areyouhere.attendee.service.dto.AttendeeData;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.dto.CourseData;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface CourseService {
    void create(Long managerId, String name, String description, List<AttendeeData> attendees, boolean onlyListNameAllowed);
    List<CourseData> getAll(Long managerId);
    void update(Long managerId, Long courseId, String name, String description, boolean onlyListNameAllowed);
    void delete(Long managerId, Long courseId);

    public Course getCourse(Long courseId);
}
