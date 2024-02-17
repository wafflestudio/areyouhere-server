package com.waruru.areyouhere.attendance.service;


import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendance.dto.UpdateAttendanceRequestDto;
import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.util.List;
import java.util.Optional;
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
    @Transactional
    public void setAttend(Long sessionId, String attendanceName){
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        Long courseId = session.getCourse().getId(); // lazy loading?
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);
        // TODO : stream 변경
        Attendee attendee = null;
        for(Attendee curAttendee: attendeesByCourseId){
            if(curAttendee.getName().equals(attendanceName)){
                attendee = curAttendee;
                break;
            }
        }
        // TODO : attendeesByCourseId null 및 empty 체크

        Attendance attendance = Attendance.builder()
                .session(session)
                .isAttended(true)
                .attendee(attendee)
                .build();
        attendanceRepository.save(attendance);
    }

    // TODO : controller 그대로 받아오지 말기.
    // TODO : N + 1
    @Transactional
    public void setAttendanceStatuses(UpdateAttendanceRequestDto updateAttendanceRequestDto){
        List<UpdateAttendance> updateAttendances = updateAttendanceRequestDto.getUpdateAttendances();
        for (UpdateAttendance updateAttendance : updateAttendances) {
            Attendance attendance = attendanceRepository.findById(updateAttendance.getAttendanceId())
                    .orElseThrow(SessionIdNotFoundException::new);
            attendance.setAttended(updateAttendance.isAttendanceStatus());
            attendanceRepository.save(attendance);
        }
    }



}
