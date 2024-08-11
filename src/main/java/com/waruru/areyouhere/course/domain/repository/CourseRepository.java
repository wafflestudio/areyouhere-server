package com.waruru.areyouhere.course.domain.repository;

import com.waruru.areyouhere.course.domain.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM course c WHERE c.manager.id = :managerId")
    List<Course> findAllByManagerId(@Param("managerId")Long managerId);

}
