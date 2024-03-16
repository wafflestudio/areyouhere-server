package com.waruru.areyouhere.attendee.domain.repository;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendeeBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    // TODO 설마 여기 courseId를 lazy로 여기서 가져오나 -> 실험
    public void insertAttendeesBatch(List<Attendee> attendees){
        jdbcTemplate.batchUpdate("INSERT INTO attendee (name, course_id) VALUES (?, ?)",
                attendees,
                attendees.size(),
                (ps, attendee) -> {
                    ps.setString(1, attendee.getName());
                    ps.setLong(2, attendee.getCourse().getId());
                });
    }
}
