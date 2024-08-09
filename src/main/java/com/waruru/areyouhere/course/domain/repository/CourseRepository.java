package com.waruru.areyouhere.course.domain.repository;

import com.waruru.areyouhere.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT course FROM course c WHERE c.manager.id = :managerId")
    List<Course> findAllByManagerId(Long managerId);

}
