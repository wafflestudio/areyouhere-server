package com.waruru.areyouhere.session.domain.entity;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value = "auth_code")
public class AuthCode {
    @Id
    @NotNull
    private String authCode;

    @NotNull
    @Indexed
    private long sessionId;

    private List<String> attendances;

}
