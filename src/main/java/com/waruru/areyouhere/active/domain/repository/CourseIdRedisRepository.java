package com.waruru.areyouhere.active.domain.repository;

import com.waruru.areyouhere.active.domain.entity.CourseId;
import org.springframework.data.repository.CrudRepository;

public interface CourseIdRedisRepository extends CrudRepository<CourseId, Long> {
}
