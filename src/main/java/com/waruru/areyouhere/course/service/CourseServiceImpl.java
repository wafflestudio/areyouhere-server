package com.waruru.areyouhere.course.service;

import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.EachClassAttendeeCountInfo;
import com.waruru.areyouhere.attendee.dto.AttendeeData;
import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.course.dto.CourseData;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.domain.repository.SessionRepository;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ManagerRepository managerRepository;
    private final SessionManager sessionManager;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final AttendeeRepository attendeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final SessionRepository sessionRepository;


    @Override
    public void create(Long managerId, String name, String description, List<AttendeeData> attendees, boolean onlyListNameAllowed) {
        Manager manager = managerRepository.findManagerById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        Course course = Course.builder()
                .manager(manager)
                .name(name)
                .description(description)
                .allowOnlyRegistered(onlyListNameAllowed)
                .build();
        courseRepository.save(course);

        List<Attendee> attendeesToSave = new ArrayList<>();

        if(!isAttendeesUnique(attendees.stream().map(attendeeData -> attendeeData.getName()
                + (attendeeData.getNote() == null ? "": attendeeData.getNote())).toList())) {
            throw new AttendeesNotUniqueException();
        }

        attendees.stream()
                .map(attendeeData -> Attendee.builder()
                        .course(course)
                        .name(attendeeData.getName())
                        .note(attendeeData.getNote())
                        .build())
                .forEach(attendeesToSave::add);

        attendeeBatchRepository.insertAttendeesBatch(attendeesToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseData> getAll(Long managerId) {
        List<Course> courses = courseRepository.findAllByManagerId(managerId);
        List<EachClassAttendeeCountInfo> eachClassAttendeeCountInfos = attendeeRepository.countAttendeesEachCourseByManagerId(
                managerId);
        Map<Long, Long> courseAttendeeCountMap = eachClassAttendeeCountInfos.stream()
                .collect(Collectors.toMap(EachClassAttendeeCountInfo::getCourseId, EachClassAttendeeCountInfo::getAttendeeCnt));

        return courses.stream()
                .map(course -> CourseData.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .allowOnlyRegistered(course.getAllowOnlyRegistered())
                        .attendees(courseAttendeeCountMap.getOrDefault(course.getId(), 0L))
                        .build()
                ).toList();
    }

    @Override
    public void update(Long managerId, Long courseId, String name, String description, boolean onlyListNameAllowed) {
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getManager().getId().equals(managerId)) {
            throw new IllegalArgumentException("Manager not authorized");
        }

        course.update(name, description, onlyListNameAllowed);
        courseRepository.save(course);
    }

    @Override
    public void delete(Long managerId, Long courseId) {
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getManager().getId().equals(managerId)) {
            throw new IllegalArgumentException("Manager not authorized");
        }
        List<Session> sessions = sessionRepository.findAllByCourseId(courseId);
        attendanceRepository.deleteAllBySessionIds(sessions.stream().map(Session::getId).toList());
        sessionRepository.deleteAllByCourseId(courseId);
        attendeeRepository.deleteAllByCourseId(courseId);
        courseRepository.delete(course);
    }

    @Override
    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    private boolean isAttendeesUnique(List<String> attendees) {
        Set<String> uniqueAttendees = Set.copyOf(attendees);
        return uniqueAttendees.size() == attendees.size();
    }

}
