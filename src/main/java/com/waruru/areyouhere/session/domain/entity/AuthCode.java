package com.waruru.areyouhere.session.domain.entity;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "auth_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {
    @Id
    @NotNull
    private String authCode;

    @NotNull
    private long sessionId;

    private List<String> attendances;

    String localDate = LocalDate.now().toString();

    @Builder
    public AuthCode(String authCode, long sessionId, List<String> attendances) {
        this.authCode = authCode;
        this.sessionId = sessionId;
        this.attendances = attendances;
    }
}
