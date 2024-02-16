package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long>{

}
