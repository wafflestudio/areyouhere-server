package com.waruru.areyouhere.course.service;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.common.utils.RandomIdentifierGenerator;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ManagerRepository managerRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
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

        Map<String, Integer> attendeeCounts = new HashMap<>();
        List<Attendee> attendeesToSave = new ArrayList<>();
        for (String attendeeName : attendees) {
            int count = attendeeCounts.getOrDefault(attendeeName, 0);
            attendeeCounts.put(attendeeName, count + 1);
            String finalName = attendeeName + (count > 0 ? randomIdentifierGenerator.generateRandomIdentifier(4) : "");
            Attendee attendee = Attendee.builder()
                    .course(course)
                    .name(finalName)
                    .build();
            attendeesToSave.add(attendee);
        }
        attendeeRepository.saveAll(attendeesToSave);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAll(Long managerId) {
        return courseRepository.findAllByManagerId(managerId);
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
    }

    @Override
    @Transactional
    public Course findCourse(Long courseId) {
        return courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found"));
    }

}
