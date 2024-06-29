package com.waruru.areyouhere.attendee.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.AttendeeAttendDetailInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.service.dto.AttendeeDetailDto;
import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: AttendeeService가 너무 방대해지고 있다. 사실 기능 분리도 함수마다 안한 편인데도 이미 크다.
// TODO: AttendeeService를 분리하고 두 layer를 두는 것이 좋을 것 같다.

@Service
@RequiredArgsConstructor
@Transactional
public class AttendeeServiceImpl implements AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final CourseRepository courseRepository;

    public void createAll(Long courseId, List<AttendeeInfo> newAttendees) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        throwIfAttendeesNameAndNoteNotUnique(newAttendees, courseId);

        List<Attendee> attendeeToUpdate = new LinkedList<>();
        List<Attendee> attendeesToSave = new LinkedList<>();

        newAttendees.forEach(attendeeInfo -> {
            if (attendeeInfo.getId() != null) {
                attendeeToUpdate.add(Attendee.builder()
                        .id(attendeeInfo.getId())
                        .course(course)
                        .name(attendeeInfo.getName())
                        .note(attendeeInfo.getNote())
                        .build());
            } else {
                attendeesToSave.add(Attendee.builder()
                        .course(course)
                        .name(attendeeInfo.getName())
                        .note(attendeeInfo.getNote())
                        .build());
            }
        });

        attendeeRepository.saveAll(attendeeToUpdate);
        attendeeBatchRepository.insertAttendeesBatch(attendeesToSave);
    }

    @Override
    public DuplicateAttendees getDuplicatesAll(Long courseId, List<String> newAttendees) {

        Set<String> uniqueAttendees = new HashSet<>();
        Set<String> duplicated = new HashSet<>();
        DuplicateAttendees duplicateAttendees = new DuplicateAttendees(new LinkedList<>());

        newAttendees.stream()
                .filter(newAttendee -> {
                    if (!uniqueAttendees.add(newAttendee)) {
                        duplicated.add(newAttendee);
                        return true;
                    }
                    return false;
                })
                .forEach(newAttendee -> duplicateAttendees.addDuplicateAttendee(null, newAttendee, null));

        duplicated.forEach(newAttendee -> duplicateAttendees.addDuplicateAttendee(null, newAttendee, null));

        getAlreadyExists(courseId, newAttendees, duplicateAttendees);
        return duplicateAttendees;
    }

    @Transactional(readOnly = true)
    public DuplicateAttendees getAlreadyExists(Long courseId, List<String> newAttendees,
                                               DuplicateAttendees duplicateAttendees) {
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);

        Set<String> attendeesAlreadyExists = Set.copyOf(newAttendees);

        attendeesByCourseId.stream()
                .filter(attendee -> attendeesAlreadyExists.contains(attendee.getName()))
                .forEach(attendee -> duplicateAttendees.addDuplicateAttendee(attendee.getId(), attendee.getName(),
                        attendee.getNote()));

        return duplicateAttendees;
    }


    // TODO : courseId 검증 -> 해당 course의 attendee인지.
    public void deleteAll(List<Long> deleteAttendees) {
        attendanceRepository.deleteAllByAttendeeIds(deleteAttendees);
        attendeeRepository.deleteAllByIds(deleteAttendees);
    }

    @Transactional(readOnly = true)
    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId) {
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionAttendees(sessionId);

        if (sessionAttendees == null || sessionAttendees.isEmpty()) {
            throw new SessionAttendeesEmptyException();
        }

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                .attendanceId(sessionAttendee.getAttendanceId())
                .name(sessionAttendee.getAttendeeName())
                .note(sessionAttendee.getAttendeeNote())
                .attendanceStatus(sessionAttendee.getAttendanceStatus())
                .attendanceTime(sessionAttendee.getAttendanceTime())
                .build()).toList();
    }

    @Transactional(readOnly = true)
    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId) {
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionOnlyAbsentee(sessionId);

        if (sessionAttendees == null || sessionAttendees.isEmpty()) {
            throw new SessionAttendeesEmptyException();
        }

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                .attendanceId(sessionAttendee.getAttendanceId())
                .name(sessionAttendee.getAttendeeName())
                .note(sessionAttendee.getAttendeeNote())
                .attendanceStatus(sessionAttendee.getAttendanceStatus())
                .attendanceTime(sessionAttendee.getAttendanceTime())
                .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId) {
        List<ClassAttendeeInfo> classAttendancesInfos = attendeeRepository.getClassAttendancesInfo(courseId);

        if (classAttendancesInfos == null || classAttendancesInfos.isEmpty()) {
            throw new ClassAttendeesEmptyException();
        }

        return classAttendancesInfos.stream().map(classAttendancesInfo -> ClassAttendees.builder()
                .id(classAttendancesInfo.getAttendeeId())
                .name(classAttendancesInfo.getName())
                .note(classAttendancesInfo.getNote())
                .attendance(classAttendancesInfo.getAttendance())
                .absence(classAttendancesInfo.getAbsence())
                .build()
        ).toList();
    }

    @Transactional(readOnly = true)
    public int getAllByCourseId(Long courseId) {
        return attendeeRepository.findAttendeesByCourse_Id(courseId).size();
    }

    @Override
    public void updateAll(Long courseId, List<AttendeeInfo> updatedAttendees) {

        throwIfAttendeesNameAndNoteNotUnique(updatedAttendees, courseId);

        updatedAttendees.stream()
                .map(attendee ->
                        Attendee.builder()
                                .id(attendee.getId())
                                .course(Course.builder().id(courseId).build())
                                .name(attendee.getName())
                                .note(attendee.getNote())
                                .build())
                .forEach(attendeeRepository::save);
    }

    // TODO: Name에 index가 없다면 위에 비해 그리 빠를 지 모르겠다.
    // FIXME: Map<String, Set<String>> 같은 것 대신 그냥 class 선언해서 쓰는게
    private void throwIfAttendeesNameAndNoteNotUnique(List<AttendeeInfo> newAttendees, Long courseId) {
        List<String> newAttendeeNames = getAttendeeUniqueNames(newAttendees);
        Map<Long, Attendee> existingAttendees = getExistingAttendeesMap(courseId, newAttendeeNames);
        checkNameSake(newAttendees, existingAttendees);
    }

    private List<String> getAttendeeUniqueNames(List<AttendeeInfo> attendees) {
        return attendees.stream()
                .map(AttendeeInfo::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    private Map<Long, Attendee> getExistingAttendeesMap(Long courseId, List<String> attendeeNames) {
        return attendeeRepository.findAttendeesByCourseIdAndNameIn(courseId, attendeeNames)
                .stream()
                .collect(Collectors.toMap(Attendee::getId, Function.identity()));
    }

    private void checkNameSake(List<AttendeeInfo> newAttendees, Map<Long, Attendee> existingAttendees) {
        Map<String, Set<String>> uniqueAttendees = new HashMap<>();

        newAttendees.forEach(attendeeInfo -> uniqueAttendees.put(attendeeInfo.getName(), new HashSet<>()));
        //FIXME: 이름도 바꾸는 경우 여기 Attendee가 없을 수 있다. 기존 db에 없는 이름인 경우.
        newAttendees.forEach(attendeeInfo -> {
            if (attendeeInfo.getId() != null) {
                existingAttendees.computeIfAbsent(attendeeInfo.getId(), id -> {
                    throw new IllegalArgumentException("Attendee not found");
                }).update(attendeeInfo.getName(), attendeeInfo.getNote());

            } else if (!checkOneAttendeeUnique(attendeeInfo, uniqueAttendees)) {
                throw new AttendeesNotUniqueException();
            }
        });

        existingAttendees.values()
                .stream()
                .filter(attendee -> !checkOneAttendeeUnique(attendee, uniqueAttendees))
                .findAny()
                .ifPresent(attendee -> {
                    throw new AttendeesNotUniqueException();
                });
    }

    private boolean checkOneAttendeeUnique(AttendeeInfo attendeeInfo, Map<String, Set<String>> uniqueAttendees) {
        return uniqueAttendees.get(attendeeInfo.getName()).add(Optional.ofNullable(attendeeInfo.getNote()).orElse(""));
    }

    private boolean checkOneAttendeeUnique(Attendee attendee, Map<String, Set<String>> uniqueAttendees) {
        return uniqueAttendees.get(attendee.getName()).add(Optional.ofNullable(attendee.getNote()).orElse(""));
    }

}
