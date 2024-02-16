package com.waruru.areyouhere.attendee.domain.repository;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    @Query(value = "SELECT attendee FROM attendee WHERE attendee.course_id = :courseId And attendee.id NOT IN (SELECT att.attendee_id FROM attendance as att WHERE att.session_id = :sessionId)", nativeQuery = true)
    public List<Attendee> findAbsenteeBySessionId(@Param("courseId") Long courseId, @Param("sessionId") Long sessionId);
}