package com.waruru.areyouhere.active.domain.repository;

import com.waruru.areyouhere.active.domain.entity.SessionId;
import org.springframework.data.repository.CrudRepository;

public interface SessionIdRedisRepository extends CrudRepository<SessionId, Long> {


}
