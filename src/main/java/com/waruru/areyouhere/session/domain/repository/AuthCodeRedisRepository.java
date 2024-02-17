package com.waruru.areyouhere.session.domain.repository;

import com.waruru.areyouhere.session.domain.entity.AuthCode;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthCodeRedisRepository extends CrudRepository<AuthCode, String>{

    public Optional<AuthCode> findAuthCodeByAuthCode(String authCode);

    public Optional<AuthCode> findAuthCodeBySessionId(long sessionId);

    public void deleteAuthCodeByAuthCode(String authCode);


}
