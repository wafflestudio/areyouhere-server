package com.waruru.areyouhere.active.domain.repository;

import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;


public interface ActiveSessionRepository extends CrudRepository<CurrentSessionAttendanceInfo, String>{
    Optional<CurrentSessionAttendanceInfo> findBySessionId(long sessionId);

    Optional<CurrentSessionAttendanceInfo> findByCourseId(long courseId);
}
