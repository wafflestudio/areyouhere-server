package com.waruru.areyouhere.active.domain.repository;

import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import org.springframework.data.repository.CrudRepository;

public interface AuthCodeRedisRepository extends CrudRepository<CurrentSessionAttendanceInfo, String>{

}
