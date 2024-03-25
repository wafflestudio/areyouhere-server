package com.waruru.areyouhere.attendance.domain.repository;

import com.waruru.areyouhere.attendance.domain.entity.AttendeeRedisData;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceRedisRepository {
    private final RedisTemplate<String, Long> redisTemplate;

    private final String ATTENDANCE_KEY = "attendance";

    public boolean isAlreadyAttended(String authCode, AttendeeRedisData attendeeInfo){
        Set<Long> attendedMembers = redisTemplate.opsForSet().members(ATTENDANCE_KEY + authCode);
        return attendedMembers == null || !attendedMembers.contains(attendeeInfo.getId());
    }

    public void setAttend(String authCode, AttendeeRedisData attendeeInfo){
        redisTemplate.opsForSet().add(ATTENDANCE_KEY + authCode, attendeeInfo.getId());
    }

    public void deleteAllAttendanceInSession(String authCode){
        redisTemplate.delete(ATTENDANCE_KEY + authCode);
    }


}


