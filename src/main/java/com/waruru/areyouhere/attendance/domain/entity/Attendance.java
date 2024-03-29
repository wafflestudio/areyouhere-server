package com.waruru.areyouhere.attendance.domain.entity;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.session.domain.entity.Session;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

//TODO: createdTime 별도 테이블로 분리


@Getter
@Entity(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

    private boolean isAttended;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Attendance(Attendee attendee, Session session, boolean isAttended) {
        this.attendee = attendee;
        this.session = session;
        this.isAttended = isAttended;
    }

    public void setAttended(boolean attended) {
        isAttended = attended;
    }
}
