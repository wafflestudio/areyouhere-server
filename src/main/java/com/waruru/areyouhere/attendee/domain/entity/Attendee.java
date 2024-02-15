package com.waruru.areyouhere.attendee.domain.entity;

import com.waruru.areyouhere.session.domain.entity.Session;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "attendee")
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    private String name;
}
