package com.waruru.areyouhere.session.domain.entity;

import com.waruru.areyouhere.course.domain.entity.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

// TODO : jpa Auditing 사용해서 baseEntity 상속하게 만들기

@Getter
@Entity(name = "session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String name;

    private boolean isDeactivated;

    @CreatedDate
    private LocalDateTime createdAt;

    public void setDeactivated(boolean deactivated) {
        isDeactivated = deactivated;
    }

    @Builder
    public Session(Long id, Course course, String name, boolean isDeactivated) {
        this.id = id;
        this.course = course;
        this.name = name;
        this.isDeactivated = isDeactivated;
    }
}
