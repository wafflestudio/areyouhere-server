package com.waruru.areyouhere.course.controller;

import com.waruru.areyouhere.course.dto.request.CourseCreationRequest;
import com.waruru.areyouhere.course.dto.request.CourseUpdateRequest;
import com.waruru.areyouhere.course.service.CourseService;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.course.domain.entity.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<Void> createCourse(
            @AuthenticationPrincipal Manager manager,
            @RequestBody CourseCreationRequest request) {
        courseService.create(manager.getId(), request.getName(), request.getDescription(), request.getAttendees(), request.isOnlyListNameAllowed());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam Long managerId) {
        List<Course> courses = courseService.getAll(managerId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Void> updateCourse(@AuthenticationPrincipal Manager manager, @PathVariable Long courseId,
                                             @RequestBody CourseUpdateRequest request) {
        courseService.update(manager.getId(), courseId, request.getName(), request.getDescription(), request.isOnlyListNameAllowed());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@AuthenticationPrincipal Manager manager
                                             , @PathVariable Long courseId) {
        courseService.delete(manager.getId(), courseId);
        return ResponseEntity.ok().build();
    }
}
