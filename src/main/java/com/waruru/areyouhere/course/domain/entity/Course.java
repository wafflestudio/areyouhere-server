package com.waruru.areyouhere.course.domain.entity;

import com.waruru.areyouhere.manager.domain.entity.Manager;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "course")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public Course(Long id, Manager manager, String name, String description, Boolean allowOnlyRegistered){
        this.id = id;
        this.manager = manager;
        this.name = name;
        this.description = description;
        this.allowOnlyRegistered = allowOnlyRegistered;
    }

    public void update(String name, String description, Boolean allowOnlyRegistered){
        this.name = name;
        this.description = description;
        this.allowOnlyRegistered = allowOnlyRegistered;
    }
}
