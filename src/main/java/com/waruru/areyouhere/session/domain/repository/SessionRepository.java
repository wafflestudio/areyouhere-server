package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.dto.SessionInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long>{


    @Query("SELECT s FROM session s WHERE s.course.id = :courseId")
    public List<Session> findAllByCourseId(@Param("courseId") Long courseId);


    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 6")
    public List<Session> findTOP6BySessionByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 1")
    public Optional<Session> findMostRecentSessionByCourseId(@Param("courseId") Long courseId);

    //find attendee who participate particular course and no attendance in particular session by query

    @Query("SELECT s.course.id FROM session s WHERE s.id = :sessionId")
    public Optional<Long> findCourseIdBySession_Id(@Param("sessionId") Long sessionId);

    // refactor
    // 단발쿼리로 나가는 것의 단점 -> 너무 request 의존적이다. 과연 속도 이점이 얼마나 될 지? 옳은 코드인지 모르겠다.
    // session list를 구해오고 그 순서대로 출석자, 결석자 수를 정렬해서 가져오는 것이 더 나은 방법이 아닐까? 어차피 쿼리는 두 번 나가는데 큰 차이도 없다.
    // 무엇보다 network IO 말고 DB 상에서 어떤 게 빠를지 고민해볼 필요도 있다.
    @Query(value = "SELECT session.id as id, session.auth_code_created_at as date, session.name as name, "
            + "COUNT(case when attendance.is_attended = true then 1 end) as attendee, "
            + "COUNT(case when attendance.is_attended = false then 1 end) as absentee \n"
            + "FROM session "
            + "INNER JOIN attendance ON session.id = attendance.session_id \n"
            + "WHERE session.course_id = :courseId \n"
            + "GROUP BY session.id", nativeQuery = true)
    public List<SessionInfo> findSessionsWithAttendance(@Param("courseId") Long courseId);

    //TODO : refactor - application단 로직에서 출석자와 결석자를 counting 한다면 조금 더 가독성있으면서 성능 차이는 없습니다.
    @Query(value = "SELECT session.id as id, session.auth_code_created_at as date, session.name as name, "
            + "COUNT(case when attendance.is_attended = true then 1 end) as attendee, "
            + "COUNT(case when attendance.is_attended = false then 1 end) as absentee \n"
            + "FROM session \n"
            + "INNER JOIN attendance ON session.id = attendance.session_id \n"
            + "WHERE session.id = :sessionId \n"
            + "GROUP BY session.id", nativeQuery = true )
    public Optional<SessionInfo> findSessionWithAttendance(@Param("sessionId") Long sessionId);

}
