package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.AuthCode;
import com.waruru.areyouhere.session.domain.entity.SessionId;
import org.springframework.data.repository.CrudRepository;

public interface SessionIdRedisRepository extends CrudRepository<SessionId, Long> {


}
