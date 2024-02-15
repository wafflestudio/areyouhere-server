package com.waruru.areyouhere.attendance.domain.entity;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.session.domain.entity.Session;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
}
