package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    // TODO: jdbcTemplate을 통한 batch insert 최적화.
    public List<Attendance> findAttendancesBySession_Id(Long sessionId);


    @Modifying
    @Query("delete from attendance a where a.attendee.id in :ids")
    public void deleteAllByAttendeeIds(@Param("ids") List<Long> ids);


}
