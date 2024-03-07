package com.waruru.areyouhere.attendance.service;


import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.AuthCodeRedisRepository;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional(readOnly = true)
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
    // TODO :redis의 이점을 누리려면 이렇게 RDB를 거쳐야 하는 친구는 다른 스레드로 보내서 비동기 처리 필요.
    // TODO : attend가 기록되기 전 유저에게는 응답이 바로 나가야 함.
    // TODO : 근데 course에 존재하는 session의 학생인지도 검증해야되는데..?
    // TODO : authCode와 책임 분리가 제대로 되고 있지 않다. 분리해야한다.
    @Transactional
    public void setAttend(Long sessionId, String attendanceName){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        Long courseId = session.getCourse().getId(); // lazy loading?
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);

        Attendee attendee = attendeesByCourseId.stream()
                .filter(s -> s.getName().equals(attendanceName))
                .findFirst()
                .orElse(null);

        // TODO : attendeesByCourseId null 및 empty 체크

        Attendance attendance = Attendance.builder()
                .session(session)
                .isAttended(true)
                .attendee(attendee)
                .build();
        attendanceRepository.save(attendance);
    }

    @Transactional
    public void setAttendanceStatuses(Long sessionId , List<UpdateAttendance> updateAttendances){

        for (UpdateAttendance updateAttendance : updateAttendances) {
            Attendance attendance = attendanceRepository.findById(updateAttendance.getAttendanceId())
                    .orElseThrow(SessionIdNotFoundException::new);
            attendance.setAttended(updateAttendance.isAttendanceStatus());
            attendanceRepository.save(attendance);
        }
    }

    @Transactional(readOnly = true)
    public int currentAttendance(Long sessionId){
        List<Attendance> attendancesBySessionId = attendanceRepository.findAttendancesBySession_Id(sessionId);
        if(attendancesBySessionId == null || attendancesBySessionId.isEmpty()){
            return 0;
        }else{
            return attendancesBySessionId.size();
        }

    }
}
