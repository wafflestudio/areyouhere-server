package com.waruru.areyouhere.attendee.domain.repository;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.dto.AttendeeAttendDetailInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.EachClassAttendeeCountInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    @Query(value = "SELECT * FROM attendee WHERE attendee.course_id = :courseId And attendee.id NOT IN (SELECT att.attendee_id FROM attendance as att WHERE att.session_id = :sessionId)", nativeQuery = true)
    public List<Attendee> findAbsenteeBySessionIdWhenNoRegister(@Param("courseId") Long courseId, @Param("sessionId") Long sessionId);

    @Query(value = "SELECT atda.id as AttendanceId, atdee.id as AttendeeId, atdee.name as AttendeeName, atdee.note as AttendeeNote, atda.is_attended as AttendanceStatus, atda.created_at as AttendanceTime \n"
            + "FROM attendee as atdee \n"
            + "INNER JOIN attendance as atda ON atdee.id = atda.attendee_id  \n"
            + "WHERE atda.session_id = :sessionId", nativeQuery = true)
    public List<SessionAttendeeInfo> findSessionAttendees(@Param("sessionId") Long sessionId);


    @Query(value = "SELECT atda.id as AttendanceId, atdee.id as AttendeeId, atdee.name as AttendeeName, atdee.note as AttendeeNote, atda.is_attended as AttendanceStatus, atda.created_at as AttendanceTime \n"
            + "FROM attendee as atdee \n"
            + "INNER JOIN attendance as atda ON atdee.id = atda.attendee_id  \n"
            + "WHERE atda.session_id = :sessionId \n"
            + "and atda.is_attended = false", nativeQuery = true)
    public List<SessionAttendeeInfo> findSessionOnlyAbsentee(@Param("sessionId") Long sessionId);


    @Query(value = "SELECT attd.id as AttendeeId, attd.name as Name, attd.note as note, "
            + "COUNT(case when atdc.is_attended = true then 1 end) as attendance, "
            + "COUNT(case when atdc.is_attended = false then 1 end) as absence \n"
            + "FROM attendee as attd \n"
            + "LEFT OUTER JOIN attendance as atdc ON attd.id = atdc.attendee_id \n"
            + "WHERE attd.course_id = :courseId \n"
            + "GROUP BY attd.id", nativeQuery = true)
    public List<ClassAttendeeInfo> getClassAttendancesInfo(@Param("courseId") Long courseId);


    public List<Attendee> findAttendeesByCourse_Id(Long courseId);

    @Query(value = "SELECT a.course_id as CourseId, COUNT(*) as AttendeeCnt \n"
            + "FROM attendee a \n"
            + "JOIN course c ON a.course_id = c.id \n"
            + "WHERE c.manager_id = :managerId \n"
            + "GROUP BY a.course_id", nativeQuery = true)
    public List<EachClassAttendeeCountInfo> countAttendeesEachCourseByManagerId(@Param("managerId") Long managerId);

    @Query(value = "SELECT atda.id as AttendanceId,  sess.id as SessionId, sess.name as SessionName, atda.is_attended as AttendanceStatus, atda.created_at as AttendanceTime\n"
            + "from attendance atda\n"
            + "INNER JOIN session as sess ON atda.session_id = sess.id\n"
            + "where atda.attendee_id = :attendeeId\n"
            + "GROUP BY atda.id", nativeQuery = true)
    public List<AttendeeAttendDetailInfo> findAttendanceInfoByAttendeeId(@Param("attendeeId") Long attendeeId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from attendee a where a.id in :ids")
    public void deleteAllByIds(@Param("ids") List<Long> ids);

    @Modifying(clearAutomatically = true)
    @Query("delete from attendee a where a.course.id = :courseId")
    public void deleteAllByCourseId(@Param("courseId") Long courseId);


    @Query(value = "SELECT * FROM attendee a WHERE a.course_id = :courseId AND a.name IN :names", nativeQuery = true)
    public List<Attendee> findAttendeesByCourseIdAndNameIn( @Param("courseId") Long courseId, @Param("names") List<String> names);

}