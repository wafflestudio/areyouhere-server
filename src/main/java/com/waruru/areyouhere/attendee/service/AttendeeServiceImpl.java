package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService{

    AttendeeRepository attendeeRepository;
    AttendanceRepository attendanceRepository;

    // 그냥 service에서 throw로 exception 던지고 no content 204 반환하는게 좋지 않으련지?
    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId){
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionAttendees(sessionId);
        return sessionAttendees == null || sessionAttendees.isEmpty() ?
                Collections.emptyList()
                : sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                        .attendeeName(sessionAttendee.getAttendeeName())
                        .attendanceStatus(sessionAttendee.getAttendanceStatus())
                        .attendanceTime(sessionAttendee.getAttendanceTime())
                        .build()).toList();
    }

    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId){
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionOnlyAbsentee(sessionId);
        return sessionAttendees == null || sessionAttendees.isEmpty() ?
                Collections.emptyList()
                : sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                        .attendeeName(sessionAttendee.getAttendeeName())
                        .attendanceStatus(sessionAttendee.getAttendanceStatus())
                        .attendanceTime(sessionAttendee.getAttendanceTime())
                        .build()
                ).toList();
    }


    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId){
        List<ClassAttendeeInfo> classAttendancesInfos = attendeeRepository.getClassAttendancesInfo(courseId);

        return classAttendancesInfos == null || classAttendancesInfos.isEmpty() ?
                Collections.emptyList()
                : classAttendancesInfos.stream().map( classAttendancesInfo -> ClassAttendees.builder()
                        .name(classAttendancesInfo.getName())
                        .attendance(classAttendancesInfo.getAttendance())
                        .absenece(classAttendancesInfo.getAbsence())
                        .build()
                ).toList();
    }

    



}
