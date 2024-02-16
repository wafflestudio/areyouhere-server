package com.waruru.areyouhere.course.domain.entity;

import com.waruru.areyouhere.manager.domain.entity.Manager;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    private String name;

    private String description;

    private Boolean allowOnlyRegistered;
}
