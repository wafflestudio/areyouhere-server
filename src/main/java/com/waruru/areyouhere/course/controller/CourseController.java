package com.waruru.areyouhere.course.controller;

import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
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
    @PostMapping
    public ResponseEntity<Void> createCourse(
            @Login Manager manager,
            @RequestBody CourseCreationRequest request) {
        courseService.create(manager.getId(), request.getName(), request.getDescription(), request.getAttendees(), request.isOnlyListNameAllowed());
        return ResponseEntity.ok().build();
    }


    @LoginRequired
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(@RequestParam Manager manager) {
        List<Course> courses = courseService.getAll(manager.getId());
        return ResponseEntity.ok(courses);
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
