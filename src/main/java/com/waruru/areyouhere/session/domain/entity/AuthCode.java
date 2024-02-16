package com.waruru.areyouhere.session.domain.entity;


import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@AllArgsConstructor
@Getter
@RedisHash(value = "auth_code")
public class AuthCode {
    @Id
    @NotNull
    private String authCode;

    @NotNull
    private long sessionId;

    @TimeToLive
    @NotNull
    private long time;
}
