package com.waruru.areyouhere.session.domain.entity;

import com.waruru.areyouhere.course.domain.entity.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Setter
    private boolean isDeactivated;

    @CreatedDate
    private LocalDateTime createdAt;

    // 논의해볼 부분 : previous Session에서 보여줄 시간은
    // authCode 생성 기점으로 갈 것인가. 아니면 session 생성 기점으로 갈 것인가.
    @Setter
    private LocalDateTime authCodeCreatedAt;


    @Builder
    public Session(Long id, Course course, String name, boolean isDeactivated) {
        this.id = id;
        this.course = course;
        this.name = name;
        this.isDeactivated = isDeactivated;
    }
}
