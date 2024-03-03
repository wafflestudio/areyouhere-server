package com.waruru.areyouhere.attendee.domain.repository;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    @Query(value = "SELECT * FROM attendee WHERE attendee.course_id = :courseId And attendee.id NOT IN (SELECT att.attendee_id FROM attendance as att WHERE att.session_id = :sessionId)", nativeQuery = true)
    public List<Attendee> findAbsenteeBySessionIdWhenNoRegister(@Param("courseId") Long courseId, @Param("sessionId") Long sessionId);

    @Query(value = "SELECT atdee.id as attendeeId, atdee.name as AttendeeName, atda.is_attended as AttendanceStatus, atda.created_at as AttendanceTime \n"
            + "FROM attendee as atdee \n"
            + "INNER JOIN attendance as atda ON atdee.id = atda.attendee_id  \n"
            + "WHERE atda.session_id = :sessionId", nativeQuery = true)
    public List<SessionAttendeeInfo> findSessionAttendees(@Param("sessionId") Long sessionId);


    @Query(value = "SELECT atdee.id as attendeeId,atdee.name as AttendeeName, atda.is_attended as AttendanceStatus, atda.created_at as AttendanceTime \n"
            + "FROM attendee as atdee \n"
            + "INNER JOIN attendance as atda ON atdee.id = atda.attendee_id  \n"
            + "WHERE atda.session_id = :sessionId \n"
            + "and atda.is_attended = false", nativeQuery = true)
    public List<SessionAttendeeInfo> findSessionOnlyAbsentee(@Param("sessionId") Long sessionId);


    @Query(value = "SELECT attd.id as attendeeId, attd.name as name, "
            + "COUNT(case when atdc.is_attended = true then 1 end) as attendance, "
            + "COUNT(case when atdc.is_attended = false then 1 end) as absence \n"
            + "FROM attendee as attd \n"
            + "LEFT OUTER JOIN attendance as atdc ON attd.id = atdc.attendee_id \n"
            + "WHERE attd.course_id = :courseId \n"
            + "GROUP BY attd.id", nativeQuery = true)
    public List<ClassAttendeeInfo> getClassAttendancesInfo(@Param("courseId") Long courseId);

    public List<Attendee> findAttendeesByCourse_Id(Long courseId);

    @Modifying
    @Query("delete from attendee a where a.id in :ids")
    public void deleteAllByIds(@Param("ids") List<Long> ids);

}