package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService{

    private final AttendeeRepository attendeeRepository;
    private final CourseRepository courseRepository;

    //TODO : stream 변경
    @Transactional
    public void createAttendees(Long courseId, List<String> newAttendees){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseIdNotFoundException::new);
        List<Attendee> attendees = new ArrayList<>();
        for(String newAttendee : newAttendees){
            attendees.add(Attendee.builder()
                    .course(course)
                    .name(newAttendee).build());
        }
        attendeeRepository.saveAll(attendees);
    }


    //TODO : refactor -> 말도 안되는 jpa 처리
    // TODO : courseId 검증 -> 해당 course의 attendee인지.
    @Transactional
    public void deleteAttendees(List<Long> deleteAttendees){

        for(Long attendeeId : deleteAttendees){
            attendeeRepository.deleteById(attendeeId);
        }

    }

    // TODO : refactor => 그냥 service에서 throw로 exception 던지고 no content 204 반환하는게 좋지 않으련지?
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId){
        List<ClassAttendeeInfo> classAttendancesInfos = attendeeRepository.getClassAttendancesInfo(courseId);

        return classAttendancesInfos == null || classAttendancesInfos.isEmpty() ?
                Collections.emptyList()
                : classAttendancesInfos.stream().map( classAttendancesInfo -> ClassAttendees.builder()
                        .id(classAttendancesInfo.getattendeeId())
                        .name(classAttendancesInfo.getName())
                        .attendance(classAttendancesInfo.getAttendance())
                        .absenece(classAttendancesInfo.getAbsence())
                        .build()
                ).toList();
    }




}
