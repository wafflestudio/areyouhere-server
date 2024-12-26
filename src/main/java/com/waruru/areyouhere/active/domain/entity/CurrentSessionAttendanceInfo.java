package com.waruru.areyouhere.active.domain.entity;

import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "auth_code")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CurrentSessionAttendanceInfo {
    @Id
    @NotNull
    private String authCode;

    @NotNull
    @Indexed
    private long sessionId;

    @NotNull
    @Indexed
    private long courseId;

    private List<AttendeeRedisData> attendees;

    @Getter
    @Setter
    private AttendanceType attendanceStatus;

    @Getter
    private Map<Long, AttendInfo> attendInfos = new HashMap<>();

    @NotNull
    @Setter
    private String courseName;

    @NotNull
    @Setter
    private String sessionName;

    private LocalDateTime createdAt;

    @Builder
    public CurrentSessionAttendanceInfo(String authCode, long sessionId, long courseId, List<Attendee> attendees, String courseName, String sessionName) {
        this.authCode = authCode;
        this.sessionId = sessionId;
        this.courseId = courseId;
        this.attendees = Optional.ofNullable(attendees)
                .orElse(Collections.emptyList())
                .stream()
                .map(attendee -> AttendeeRedisData.builder()
                        .id(attendee.getId())
                        .name(attendee.getName())
                        .note(attendee.getNote())
                        .build())
                .toList();
        this.courseName = courseName;
        this.sessionName = sessionName;
        this.createdAt = LocalDateTime.now();
        this.attendanceStatus = AttendanceType.ATTENDED;
    }

    public void updateAttendees(List<Attendee> attendees){
        this.attendees = Optional.ofNullable(attendees)
                .orElse(Collections.emptyList())
                .stream()
                .map(attendee -> AttendeeRedisData.builder()
                        .id(attendee.getId())
                        .name(attendee.getName())
                        .note(attendee.getNote())
                        .build())
                .toList();
    }

    public void setAttendInfos(Long attendeeId, AttendanceType status, LocalDateTime time){
        attendInfos.put(attendeeId, new AttendInfo(time, status));
    }

    public void removeAttendanceTime(Long attendeeId){
        attendInfos.remove(attendeeId);
    }

    public Set<Long> getAttendAttendeesIds(){
        return attendInfos.keySet();
    }

    public boolean isAlreadyAttended(Long attendeeId){
        return attendInfos.containsKey(attendeeId);
    }

}


