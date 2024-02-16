package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.Session;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionRepository extends JpaRepository<Session, Long>{

    //TODO: query 수정 -> course가 조회가 어떻게 되는지 의심스러움.
    @Query("SELECT s FROM session s WHERE s.course.id = :courseId")
    public List<Session> findAllByCourseId(Long courseId);


    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 5")
    public List<Session> findTOP5BySessionByCourseId(Long courseId);

    @Query("SELECT s FROM session s WHERE s.course.id = :courseId order by s.createdAt desc limit 1")
    public Optional<Session> findMostRecentSessionByCourseId(Long courseId);

}
