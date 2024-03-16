package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.exception.AttendeeAlreadyExistsException;
import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService{

    private final AttendeeRepository attendeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void createAttendees(Long courseId, List<String> newAttendees){

        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseIdNotFoundException::new);

        List<String> attendeesNames = attendeeRepository.findAttendeesByCourse_Id(courseId).stream()
                .map(Attendee::getName)
                .toList();

        Set<String> attendeesAlreadyExists = Set.copyOf(attendeesNames);

        if(!isAttendeesUnique(newAttendees))
            throw new AttendeesNotUniqueException("참여자 이름이 중복되었습니다.");

        newAttendees
                .forEach(attendee ->
                        {
                            if(attendeesAlreadyExists.contains(attendee))
                                throw new AttendeeAlreadyExistsException("이미 등록된 참여자가 존재합니다.");
                        }
                );

        List<Attendee> attendees = newAttendees.stream()
                .map(newAttendee -> Attendee.builder()
                        .course(course)
                        .name(newAttendee).build())
                .toList();

        attendeeBatchRepository.insertAttendeesBatch(attendees);
    }



    // TODO : courseId 검증 -> 해당 course의 attendee인지.
    @Transactional
    public void deleteAttendees(List<Long> deleteAttendees){
        attendanceRepository.deleteAllByAttendeeIds(deleteAttendees);
        attendeeRepository.deleteAllByIds(deleteAttendees);
    }

    @Transactional(readOnly = true)
    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId){
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionAttendees(sessionId);

        if(sessionAttendees == null || sessionAttendees.isEmpty())
            throw new SessionAttendeesEmptyException();

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                        .attendanceId(sessionAttendee.getAttendanceId())
                        .attendeeName(sessionAttendee.getAttendeeName())
                        .attendanceStatus(sessionAttendee.getAttendanceStatus())
                        .attendanceTime(sessionAttendee.getAttendanceTime())
                        .build()).toList();
    }

    @Transactional(readOnly = true)
    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId){
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionOnlyAbsentee(sessionId);

        if(sessionAttendees == null || sessionAttendees.isEmpty())
            throw new SessionAttendeesEmptyException();

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                        .attendanceId(sessionAttendee.getAttendanceId())
                        .attendeeName(sessionAttendee.getAttendeeName())
                        .attendanceStatus(sessionAttendee.getAttendanceStatus())
                        .attendanceTime(sessionAttendee.getAttendanceTime())
                        .build()
                ).toList();
    }

    @Transactional(readOnly = true)
    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId){
        List<ClassAttendeeInfo> classAttendancesInfos = attendeeRepository.getClassAttendancesInfo(courseId);

        if(classAttendancesInfos == null || classAttendancesInfos.isEmpty())
            throw new ClassAttendeesEmptyException();

        return classAttendancesInfos.stream().map( classAttendancesInfo -> ClassAttendees.builder()
                        .id(classAttendancesInfo.getattendeeId())
                        .name(classAttendancesInfo.getName())
                        .attendance(classAttendancesInfo.getAttendance())
                        .absence(classAttendancesInfo.getAbsence())
                        .build()
                ).toList();
    }

    @Transactional
    public int getAttendeeByCourseId(Long courseId){
        return attendeeRepository.findAttendeesByCourse_Id(courseId).size();
    }

    private boolean isAttendeesUnique(List<String> attendees) {
        Set<String> uniqueAttendees = Set.copyOf(attendees);
        return uniqueAttendees.size() == attendees.size();
    }


}
