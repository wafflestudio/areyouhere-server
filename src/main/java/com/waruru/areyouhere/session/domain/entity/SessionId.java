package com.waruru.areyouhere.session.domain.entity;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "session_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionId {
    @Id
    @NotNull
    private long sessionId;

    @NotNull
    private String authCode;

    @Builder
    public SessionId(long sessionId, String authCode) {
        this.sessionId = sessionId;
        this.authCode = authCode;
    }
}
