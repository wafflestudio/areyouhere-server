package com.waruru.areyouhere.attendance.domain.entity;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "auth_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentSessionAttendanceInfo {
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

    private LocalDateTime createdAt;

    @Builder
    public CurrentSessionAttendanceInfo(String authCode, long sessionId, List<Attendee> attendees, String courseName, String sessionName) {
        this.authCode = authCode;
        this.sessionId = sessionId;
        this.attendees = Optional.ofNullable(attendees)
                .orElse(Collections.emptyList())
                .stream()
                .map(attendee -> AttendeeRedisData.builder()
                        .id(attendee.getId())
                        .name(attendee.getName())
                        .note(attendee.getNote())
                        .build())
                .collect(Collectors.toList());
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.createdAt = LocalDateTime.now();
    }
}
