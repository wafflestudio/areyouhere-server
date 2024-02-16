package com.waruru.areyouhere.attendance.service;


import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public AttendanceCount getAttendanceCount(long sessionId) {
        List<Attendance> attendancesBySessionId = attendanceRepository.findAttendancesBySession_Id(sessionId);

        if(attendancesBySessionId.isEmpty()){
            return new AttendanceCount(0, 0);
        }
        int attendanceCount = 0;
        int absenceCount = 0;

        for (Attendance attendance : attendancesBySessionId) {
            if(attendance.isAttended()){
                attendanceCount++;
            }else{
                absenceCount++;
            }
        }
        return new AttendanceCount(attendanceCount, absenceCount);
    }

    @Override
    @Transactional
    public void setAbsentAfterDeactivation(long courseId, long sessionId){


        List<Attendee> absenteeBySessionId = attendeeRepository.findAbsenteeBySessionIdWhenNoRegister(courseId, sessionId);

        Session session = sessionRepository.findById(sessionId).orElseThrow(SessionIdNotFoundException::new);
        List<Attendance> attendances = absenteeBySessionId.stream().map(attendee -> Attendance.builder()
                .attendee((attendee))
                .session(session)
                .isAttended(false)
                .build()).toList();
        attendanceRepository.saveAll(attendances);
    }

    @Async
    public void setAttend(Long courseId, Long sessionId, String attendanceName){

        List<Attendee> absenteeBySessionId = attendeeRepository.findAbsenteeBySessionId(courseId, sessionId);

        Session session = sessionRepository.findById(sessionId).orElseThrow(SessionIdNotFoundException::new);
        List<Attendance> attendances = absenteeBySessionId.stream().map(attendee -> Attendance.builder()
                .attendee((attendee))
                .session(session)
                .isAttended(false)
                .build()).toList();
        attendanceRepository.saveAll(attendances);
    }

}
