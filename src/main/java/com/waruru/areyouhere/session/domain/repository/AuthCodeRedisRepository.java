package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.CurrentSessionAttendanceInfo;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthCodeRedisRepository extends CrudRepository<CurrentSessionAttendanceInfo, String>{

}
