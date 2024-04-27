package com.waruru.areyouhere.manager.domain.repository;

import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VerifyCodeRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final RandomIdentifierGenerator randomIdentifierGenerator;

    public String saveAndGetCode(String email){
        String verificationCode = randomIdentifierGenerator.generateRandomIdentifier(6);
        redisTemplate.opsForValue().set(email, verificationCode);
        redisTemplate.opsForValue().getAndExpire(email, 30, TimeUnit.MINUTES);
        return verificationCode;
    }

    public void saveVerification(String email){
        redisTemplate.opsForValue().set(email, "verified");
    }

    public String findByEmail(String email){
        return redisTemplate.opsForValue().get(email);
    }
    public void deleteByEmail(String email){
        redisTemplate.delete(email);
    }

    public boolean isVerified(String email){
        String verifyCode = redisTemplate.opsForValue().get(email);
        return verifyCode != null && verifyCode.equals("verified");
    }
}
