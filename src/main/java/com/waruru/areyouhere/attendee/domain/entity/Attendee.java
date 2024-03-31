package com.waruru.areyouhere.attendee.domain.entity;

import com.waruru.areyouhere.course.domain.entity.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "attendee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String name;

    private String note;

    @Builder
    public Attendee(Long id, Course course, String name, String note){
        this.id = id;
        this.course = course;
        this.name = name;
        this.note = note;
    }

    public void update(String name, String note){
        this.name = name;
        this.note = note;
    }


}
