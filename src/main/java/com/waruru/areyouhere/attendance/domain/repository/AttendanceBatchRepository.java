package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceBatchRepository {
    private final JdbcTemplate jdbcTemplate;

    @Modifying(clearAutomatically = true)
    public void insertAttendanceBatch(List<Attendance> attendances) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id) VALUES (?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, attendance.isAttended());
                    ps.setLong(2, attendance.getAttendee().getId());
                    ps.setLong(3, attendance.getSession().getId());
                });
    }

    @Modifying(clearAutomatically = true)
    public void insertAttendanceBatch(List<AttendeeRedisData> attendances, Boolean isAttended, Long sessionId) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id) VALUES (?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, isAttended);
                    ps.setLong(2, attendance.getId());
                    ps.setLong(3, sessionId);
                });
    }
}
