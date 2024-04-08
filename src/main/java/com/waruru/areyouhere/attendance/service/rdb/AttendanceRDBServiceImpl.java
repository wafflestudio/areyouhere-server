package com.waruru.areyouhere.attendance.service.rdb;


import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceRDBServiceImpl implements AttendanceRDBService {

    private final AttendanceRepository attendanceRepository;
    private final AttendeeRepository attendeeRepository;
    private final SessionRepository sessionRepository;

    @Override
    public void setAbsentAfterDeactivation(long courseId, long sessionId) {

        List<Attendee> absenteeBySessionId = attendeeRepository.findAbsenteeBySessionIdWhenNoRegister(courseId,
                sessionId);
        Session session = sessionRepository.findById(sessionId).orElseThrow(SessionIdNotFoundException::new);
        List<Attendance> attendances = absenteeBySessionId.stream().map(attendee -> Attendance.builder()
                .attendee((attendee))
                .session(session)
                .isAttended(false)
                .build()).toList();
        attendanceRepository.saveAll(attendances);
    }

    @Async
    public void setAttend(Long sessionId, String attendanceName, Long attendeeId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionIdNotFoundException::new);
        Long courseId = session.getCourse().getId(); // lazy loading?
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);

        Attendee attendee = getAttendee(attendanceName, attendeeId,
                attendeesByCourseId);

        // TODO : attendeesByCourseId null 및 empty 체크

        Attendance attendance = Attendance.builder()
                .session(session)
                .isAttended(true)
                .attendee(attendee)
                .build();
        attendanceRepository.save(attendance);

    }


    public void setAttendanceStatuses(Long sessionId, List<UpdateAttendance> updateAttendances) {
        List<Attendance> attendancesToUpdate = updateAttendances.stream()
                .map(updateAttendance -> {
                    Attendance attendance = attendanceRepository.findById(updateAttendance.getAttendanceId())
                            .orElseThrow(SessionIdNotFoundException::new);
                    attendance.setAttended(updateAttendance.isAttendanceStatus());

                    return attendance;
                })
                .collect(Collectors.toList());

        attendanceRepository.saveAll(attendancesToUpdate);
    }

    @Transactional(readOnly = true)
    public int currentAttendance(Long sessionId) {
        List<Attendance> attendancesBySessionId = attendanceRepository.findAttendancesBySession_Id(sessionId);
        if (attendancesBySessionId == null || attendancesBySessionId.isEmpty()) {
            return 0;
        } else {
            return attendancesBySessionId.size();
        }
    }


    // AttendeeId가 null이 아니라는 것은 duplicatedAttendee가 발생하여 Id 기준으로 찾아야 한다는 것.
    private Attendee getAttendee(String attendanceName, Long attendeeId, List<Attendee> attendeesByCourseId) {
        return attendeeId == null ?
                attendeesByCourseId.stream()
                        .filter(s -> s.getName().equals(attendanceName))
                        .findFirst()
                        .orElse(null) :
                attendeesByCourseId.stream()
                        .filter(s -> s.getId().equals(attendeeId))
                        .findFirst()
                        .orElse(null);
    }


}
