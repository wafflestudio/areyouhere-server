package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.active.domain.entity.AttendInfo;
import com.waruru.areyouhere.attendance.domain.entity.AttendanceType;
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
    public void insertAbsentBatch(List<AttendeeRedisData> attendances, Boolean isAttended, Long sessionId, LocalDateTime absentTime) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id, created_at, status) VALUES (?, ?, ?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, isAttended);
                    ps.setLong(2, attendance.getId());
                    ps.setLong(3, sessionId);
                    ps.setObject(4, Timestamp.valueOf(absentTime));
                    ps.setObject(5, AttendanceType.ABSENT);
                });
    }

    @Modifying(clearAutomatically = true)
    public void insertAttendBatch(List<AttendeeRedisData> attendances, Boolean isAttended, Long sessionId, Map<Long, AttendInfo> attendInfo) {
        jdbcTemplate.batchUpdate("INSERT INTO attendance (is_attended, attendee_id, session_id, created_at, status) VALUES (?, ?, ?, ?, ?)",
                attendances,
                attendances.size(),
                (ps, attendance) -> {
                    ps.setBoolean(1, isAttended);
                    ps.setLong(2, attendance.getId());
                    ps.setLong(3, sessionId);
                    ps.setObject(4, Timestamp.valueOf(attendInfo.get(attendance.getId()).getAttendTime()));
                    ps.setObject(5, attendInfo.get(attendance.getId()).getAttendanceType());
                });
    }
}
