package com.waruru.areyouhere.course.controller;

import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.course.dto.CourseData;
import com.waruru.areyouhere.course.dto.response.AllCourseResponse;
import com.waruru.areyouhere.course.dto.response.CourseGetResponse;
import com.waruru.areyouhere.course.dto.request.CourseCreationRequest;
import com.waruru.areyouhere.course.dto.request.CourseUpdateRequest;
import com.waruru.areyouhere.course.service.CourseService;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.course.domain.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @LoginRequired
    @GetMapping("/{courseId}")
    ResponseEntity<CourseGetResponse> getCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.ok(CourseGetResponse.from(course));
    }

    @LoginRequired
    @PostMapping
    public ResponseEntity<Void> createCourse(
            @Login Manager manager,
            @RequestBody CourseCreationRequest request) {
        courseService.create(manager.getId(), request.getName(), request.getDescription(), request.getAttendees(), request.isOnlyListNameAllowed());
        return ResponseEntity.ok().build();
    }


    @LoginRequired
    @GetMapping
    public ResponseEntity<AllCourseResponse> getAllCourses(@Login Manager manager) {
        List<Course> courses = courseService.getAll(manager.getId());
        List<CourseData> courseData = courses.stream()
                .map(course -> CourseData.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .allowOnlyRegistered(course.getAllowOnlyRegistered())
                        .build()).toList();
        AllCourseResponse response = new AllCourseResponse(courseData);

        return ResponseEntity.ok(response);
    }

    @LoginRequired
    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@Login Manager manager, @PathVariable Long courseId,
                                             @RequestBody CourseUpdateRequest request) {
        courseService.update(manager.getId(), courseId, request.getName(), request.getDescription(), request.isOnlyListNameAllowed());
        return ResponseEntity.ok().build();
    }

    @LoginRequired
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@Login Manager manager
                                             , @PathVariable Long courseId) {
        courseService.delete(manager.getId(), courseId);
        return ResponseEntity.ok().build();
    }
}
