package com.waruru.areyouhere.attendance.domain.entity;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "auth_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthCode {
    @Id
    @NotNull
    private String authCode;

    @NotNull
    private long sessionId;

    private List<AttendeeRedisData> attendees;

    @NotNull
    private String courseName;

    @NotNull
    private String sessionName;

    private String createdAt;

    @Builder
    public AuthCode(String authCode, long sessionId, List<Attendee> attendees, String courseName, String sessionName,
                    String createdAt) {
        this.authCode = authCode;
        this.sessionId = sessionId;
        this.attendees = attendees.stream().map(attendee ->
                AttendeeRedisData.builder()
                        .id(attendee.getId())
                        .name(attendee.getName())
                        .note(attendee.getNote())
                        .build()
        ).toList();
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.createdAt = createdAt;
    }
}
