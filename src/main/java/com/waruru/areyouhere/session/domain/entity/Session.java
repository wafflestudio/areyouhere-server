package com.waruru.areyouhere.session.domain.entity;

import com.waruru.areyouhere.course.domain.entity.Course;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Getter
@Entity(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;
}
