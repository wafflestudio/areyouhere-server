package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long>{

    //TODO: query 수정 -> course가 조회가 어떻게 되는지 의심스러움.
    @Query("SELECT s FROM session s WHERE s.course.id = :courseId")
    public List<Session> findAllByCourseId(@Param("courseId") Long courseId);


    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 5")
    public List<Session> findTOP5BySessionByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 1")
    public Optional<Session> findMostRecentSessionByCourseId(@Param("courseId") Long courseId);

    //find attendee who participate particular course and no attendance in particular session by query

    @Query("SELECT s.course.id FROM session s WHERE s.id = :sessionId")
    public Optional<Long> findCourseIdBySession_Id(@Param("sessionId") Long sessionId);

}
