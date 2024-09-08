package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>{
    public List<Attendance> findAttendancesBySession_Id(Long sessionId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from attendance a where a.attendee.id in :ids")
    public void deleteAllByAttendeeIds(@Param("ids") List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("delete from attendance a where a.session.id = :sessionId")
    public void deleteAllBySessionId(@Param("sessionId") Long sessionId);

    @Modifying(clearAutomatically = true)
    @Query("delete from attendance a where a.session.id in :ids")
    public void deleteAllBySessionIds(@Param("ids") List<Long> ids);


    // TODO: 이런 쿼리를 사용하는게 맞는지에 대한 의문. -> 일단 사용하지 맙시다.
    @Query(nativeQuery = true, value = "SELECT case when EXISTS"
            + "(SELECT 1 FROM course WHERE course.id = "
            + "(SELECT course_id FROM session WHERE id = "
            + "(SELECT session_id FROM attendance WHERE id = :attendanceId)) and manager_id = :managerId)"
            + "then 'true' else 'false' end")
    public boolean isAttendanceOwnByManagerId(@Param("managerId") Long managerId, @Param("attendanceId") Long attendanceId);

    @Query(nativeQuery = true, value = "SELECT session_id FROM attendance WHERE id = :attendanceId")
    public Optional<Long> getSessionIdByAttendanceId(@Param("attendanceId") Long attendanceId);
}
