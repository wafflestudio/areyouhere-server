package com.waruru.areyouhere.active.domain.entity;


import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "course_id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseId {
    @Id
    @NotNull
    private long courseId;

    @NotNull
    private String authCode;

    @Builder
    public CourseId(long courseId, String authCode) {
        this.courseId = courseId;
        this.authCode = authCode;
    }
}
