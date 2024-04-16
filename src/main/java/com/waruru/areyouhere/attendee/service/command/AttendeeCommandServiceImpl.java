package com.waruru.areyouhere.attendee.service.command;

import com.waruru.areyouhere.active.service.ActiveAttendanceService;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.exception.AttendeeNotFoundException;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.attendee.service.dto.AttendeeInfo;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.exception.CourseNotFoundException;
import com.waruru.areyouhere.session.exception.ActivatedSessionExistsException;
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

@Service
@RequiredArgsConstructor
@Transactional
public class AttendeeCommandServiceImpl implements AttendeeCommandService{

    private final AttendeeRepository attendeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final CourseRepository courseRepository;
    private final ActiveAttendanceService activeAttendanceService;

    @Override
    public void createAll(Long courseId, List<AttendeeInfo> newAttendees){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        throwIfAttendeesNameAndNoteNotUnique(newAttendees, courseId);

        List<Attendee> attendeeToUpdate = new LinkedList<>();
        List<Attendee> attendeesToSave = new LinkedList<>();

        newAttendees.forEach(attendeeInfo -> {
            if(attendeeInfo.getId() != null){
                attendeeToUpdate.add(Attendee.builder()
                        .id(attendeeInfo.getId())
                        .course(course)
                        .name(attendeeInfo.getName())
                        .note(attendeeInfo.getNote())
                        .build());
            }else{
                attendeesToSave.add(Attendee.builder()
                        .course(course)
                        .name(attendeeInfo.getName())
                        .note(attendeeInfo.getNote())
                        .build());
            }
        });

        attendeeRepository.saveAll(attendeeToUpdate);
        attendeeBatchRepository.insertAttendeesBatch(attendeesToSave);
        syncToRedis(courseId);
    }

    @Override
    public void updateAll(Long courseId, List<AttendeeInfo> updatedAttendees){

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
        attendeeRepository.findAttendeesByCourse_Id(courseId);
        syncToRedis(courseId);
    }

    public void deleteAll(List<Long> deleteAttendees){
        Long courseId = attendeeRepository.findById(deleteAttendees.get(0))
                .orElseThrow(() -> new AttendeeNotFoundException("Attendee not found"))
                .getCourse().getId();
        if(activeAttendanceService.isSessionActivatedByCourseId(courseId)){
            throw new ActivatedSessionExistsException();
        }
        attendanceRepository.deleteAllByAttendeeIds(deleteAttendees);
        attendeeRepository.deleteAllByIds(deleteAttendees);
    }

    private void syncToRedis(Long courseId){
        List<Attendee> attendees = attendeeRepository.findAttendeesByCourse_Id(courseId);
        activeAttendanceService.updateAttendees(courseId, attendees);
    }


    // TODO: Name에 index가 없다면 위에 비해 그리 빠를 지 모르겠다.
    // FIXME: Map<String, Set<String>> 같은 것 대신 그냥 class 선언해서 쓰는게
    private void throwIfAttendeesNameAndNoteNotUnique(List<AttendeeInfo> newAttendees, Long courseId) {
        List<String> newAttendeeNames = getAttendeeUniqueNames(newAttendees);
        Map<Long, Attendee> existingAttendees = getExistingAttendeesMap(courseId, newAttendeeNames);
        setUpdatedNameAttendees(existingAttendees, newAttendees);
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

    private void setUpdatedNameAttendees(Map<Long, Attendee> existingAttendees, List<AttendeeInfo> newAttendees) {
        newAttendees.forEach(attendeeInfo -> {
            if (attendeeInfo.getId() != null) {
                if(!existingAttendees.containsKey(attendeeInfo.getId())){
                    existingAttendees.put(attendeeInfo.getId(), Attendee.builder()
                            .id(attendeeInfo.getId())
                            .name(attendeeInfo.getName())
                            .note(attendeeInfo.getNote())
                            .build());
                }
            }
        });

    }

    private void checkNameSake(List<AttendeeInfo> newAttendees, Map<Long, Attendee> existingAttendees) {
        Map<String, Set<String>> uniqueAttendees = new HashMap<>();

        newAttendees.forEach(attendeeInfo -> uniqueAttendees.put(attendeeInfo.getName(), new HashSet<>()));

        newAttendees.forEach(attendeeInfo -> {
            if (attendeeInfo.getId() != null) {
                existingAttendees.computeIfAbsent(attendeeInfo.getId(), id -> {
                    throw new IllegalArgumentException("Attendee not found");
                }).update(attendeeInfo.getName(), attendeeInfo.getNote());

            } else if (!addUniqueNameAndNote(attendeeInfo, uniqueAttendees)) {
                throw new AttendeesNotUniqueException();
            }
        });

        existingAttendees.values()
                .stream()
                .filter(attendee -> !addUniqueNameAndNote(attendee, uniqueAttendees))
                .findAny()
                .ifPresent(attendee -> {
                    throw new AttendeesNotUniqueException();
                });
    }

    private boolean addUniqueNameAndNote(AttendeeInfo attendeeInfo, Map<String, Set<String>> uniqueAttendees) {
        return uniqueAttendees
                .get(attendeeInfo.getName())
                .add(Optional.ofNullable(attendeeInfo.getNote()).orElse(""));
    }

    private boolean addUniqueNameAndNote(Attendee attendee, Map<String, Set<String>> uniqueAttendees) {
        return uniqueAttendees
                .get(attendee.getName())
                .add(Optional.ofNullable(attendee.getNote()).orElse(""));
    }
}
