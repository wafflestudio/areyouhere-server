package com.waruru.areyouhere.course.service;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeBatchRepository;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.EachClassAttendeeCountInfo;
import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.attendee.exception.AttendeesNotUniqueException;
import com.waruru.areyouhere.course.dto.CourseData;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ManagerRepository managerRepository;
    private final SessionManager sessionManager;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
    private final AttendeeBatchRepository attendeeBatchRepository;
    private final AttendeeRepository attendeeRepository;

    @Override
    @Transactional
    public void create(Long managerId, String name, String description, List<String> attendees, boolean onlyListNameAllowed) {
        Manager manager = managerRepository.findManagerById(managerId).orElseThrow(() -> new IllegalArgumentException("Manager not found"));

        Course course = Course.builder()
                .manager(manager)
                .name(name)
                .description(description)
                .allowOnlyRegistered(onlyListNameAllowed)
                .build();
        courseRepository.save(course);

        List<Attendee> attendeesToSave = new ArrayList<>();

        if(!isAttendeesUnique(attendees)){
            throw new AttendeesNotUniqueException();
        }

        attendees.stream()
                .map(attendeeName -> Attendee.builder()
                        .course(course)
                        .name(attendeeName)
                        .build())
                .forEach(attendeesToSave::add);

        attendeeBatchRepository.insertAttendeesBatch(attendeesToSave);
        sessionManager.addCourseId(course.getId());

    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseData> getAll(Long managerId) {
        List<Course> courses = courseRepository.findAllByManagerId(managerId);
        List<EachClassAttendeeCountInfo> eachClassAttendeeCountInfos = attendeeRepository.countAttendeesEachCourseByManagerId(
                managerId);
        log.info("eachClassAttendeeCountInfos: {}", eachClassAttendeeCountInfos);
        Map<Long, Long> courseAttendeeCountMap = eachClassAttendeeCountInfos.stream()
                .collect(Collectors.toMap(EachClassAttendeeCountInfo::getCourseId, EachClassAttendeeCountInfo::getAttendeeCnt));
        log.info("courseAttendeeCountMap: {}", courseAttendeeCountMap);

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
    @Transactional
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
    @Transactional
    public void delete(Long managerId, Long courseId) {
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getManager().getId().equals(managerId)) {
            throw new IllegalArgumentException("Manager not authorized");
        }

        courseRepository.delete(course);

        sessionManager.removeCourseId(courseId);
    }

    @Override
    @Transactional
    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

    private boolean isAttendeesUnique(List<String> attendees) {
        Set<String> uniqueAttendees = Set.copyOf(attendees);
        return uniqueAttendees.size() == attendees.size();
    }

}
