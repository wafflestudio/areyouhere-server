package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.dto.AttendeeData;
import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.session.exception.CourseIdNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendeeServiceImpl implements AttendeeService{

    private final AttendeeRepository attendeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final CourseRepository courseRepository;

    public void createAttendees(Long courseId, List<AttendeeData> newAttendees){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseIdNotFoundException::new);

        List<String> attendeeUniqueCheck = newAttendees.stream()
                .map(attendeeData ->
                    attendeeData.getName() + (attendeeData.getNote() == null ? "" : attendeeData.getNote())
                ).toList();

        if(!isAttendeesUnique(attendeeUniqueCheck)){
            throw new AttendeesNotUniqueException("참여자 이름이 중복되었습니다.");
        }


        List<Attendee> attendees = newAttendees.stream()
                .map(newAttendee -> Attendee.builder()
                        .course(course)
                        .name(newAttendee.getName())
                        .note(newAttendee.getNote()).build())
                        .toList();

        attendeeBatchRepository.insertAttendeesBatch(attendees);
    }

    public DuplicateAttendees getDuplicateAttendees(Long courseId, List<String> newAttendees){
        Set<String> uniqueAttendees = new HashSet<>();
        DuplicateAttendees duplicateAttendees = new DuplicateAttendees(new LinkedList<>());

        newAttendees.stream()
                .filter(newAttendee -> !uniqueAttendees.add(newAttendee))
                .forEach(newAttendee -> duplicateAttendees.addDuplicateAttendee(null, newAttendee, null));

        getAlreadyExistsAttendees(courseId, newAttendees, duplicateAttendees);
        return duplicateAttendees;
    }

    @Transactional(readOnly = true)
    public DuplicateAttendees getAlreadyExistsAttendees(Long courseId, List<String> newAttendees, DuplicateAttendees duplicateAttendees){
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);

        Set<String> attendeesAlreadyExists = Set.copyOf(newAttendees);

        attendeesByCourseId.stream()
                .filter(attendee -> attendeesAlreadyExists.contains(attendee.getName()))
                .forEach(attendee -> duplicateAttendees.addDuplicateAttendee(attendee.getId(),attendee.getName(), attendee.getNote()));

        return duplicateAttendees;
    }


    // TODO : courseId 검증 -> 해당 course의 attendee인지.
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
                        .id(sessionAttendee.getAttendanceId())
                        .name(sessionAttendee.getAttendeeName())
                        .note(sessionAttendee.getAttendeeNote())
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
                        .id(sessionAttendee.getAttendanceId())
                        .name(sessionAttendee.getAttendeeName())
                        .note(sessionAttendee.getAttendeeNote())
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
                        .id(classAttendancesInfo.getAttendeeId())
                        .name(classAttendancesInfo.getName())
                        .note(classAttendancesInfo.getNote())
                        .attendance(classAttendancesInfo.getAttendance())
                        .absence(classAttendancesInfo.getAbsence())
                        .build()
                ).toList();
    }

    @Transactional(readOnly = true)
    public int getAttendeeByCourseId(Long courseId){
        return attendeeRepository.findAttendeesByCourse_Id(courseId).size();
    }

    private boolean isAttendeesUnique(List<String> attendees) {
        Set<String> uniqueAttendees = Set.copyOf(attendees);
        return uniqueAttendees.size() == attendees.size();
    }



}
