package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceBatchRepository {
    private final JdbcTemplate jdbcTemplate;



    @Modifying(clearAutomatically = true)
    public void insertAbsentBatch(List<AttendeeRedisData> attendances, Boolean isAttended, Long sessionId, LocalDateTime currentTime) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id, created_at) VALUES (?, ?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, isAttended);
                    ps.setLong(2, attendance.getId());
                    ps.setLong(3, sessionId);
                    ps.setObject(4, Timestamp.valueOf(currentTime));
                });
    }

    @Modifying(clearAutomatically = true)
    public void insertAttendBatch(List<AttendeeRedisData> attendances, Boolean isAttended, Long sessionId, Map<Long, LocalDateTime> attendanceTime) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id, created_at) VALUES (?, ?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, isAttended);
                    ps.setLong(2, attendance.getId());
                    ps.setLong(3, sessionId);
                    ps.setObject(4, Timestamp.valueOf(attendanceTime.get(attendance.getId())));
                });
    }
}
