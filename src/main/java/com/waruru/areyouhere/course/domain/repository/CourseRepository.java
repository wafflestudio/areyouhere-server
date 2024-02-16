package com.waruru.areyouhere.course.domain.repository;

import com.waruru.areyouhere.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByManagerId(Long managerId);

}
