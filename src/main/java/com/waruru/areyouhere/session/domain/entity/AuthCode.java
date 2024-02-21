package com.waruru.areyouhere.session.domain.entity;

import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    private String attendances;

    private String createdAt;

    public List<String> getAttendances() {
        return  Arrays.asList(attendances.substring(1, attendances.length() - 1).split(", "));
    }

    @Builder
    public AuthCode(String authCode, long sessionId, List<String> attendances, String createdAt) {
        this.authCode = authCode;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.attendances = attendances.toString();
    }
}
