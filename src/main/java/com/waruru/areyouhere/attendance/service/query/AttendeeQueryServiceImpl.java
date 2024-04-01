package com.waruru.areyouhere.attendance.service.query;

import com.waruru.areyouhere.attendee.domain.entity.Attendee;
import com.waruru.areyouhere.attendee.domain.repository.AttendeeRepository;
import com.waruru.areyouhere.attendee.domain.repository.dto.AttendeeAttendDetailInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.ClassAttendeeInfo;
import com.waruru.areyouhere.attendee.domain.repository.dto.SessionAttendeeInfo;
import com.waruru.areyouhere.attendee.exception.ClassAttendeesEmptyException;
import com.waruru.areyouhere.attendee.exception.SessionAttendeesEmptyException;
import com.waruru.areyouhere.attendee.service.dto.AttendeeDetailDto;
import com.waruru.areyouhere.attendee.service.dto.ClassAttendees;
import com.waruru.areyouhere.attendee.service.dto.DuplicateAttendees;
import com.waruru.areyouhere.attendee.service.dto.SessionAttendees;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendeeQueryServiceImpl implements AttendeeQueryService{

    private final AttendeeRepository attendeeRepository;
    @Override
    public AttendeeDetailDto getAttendanceCount(Long attendeeId) {
        Attendee attendee = attendeeRepository.findById(attendeeId)
                .orElseThrow(() -> new IllegalArgumentException("참여자가 존재하지 않습니다."));

        List<AttendeeAttendDetailInfo> attendanceInfoByAttendeeId = attendeeRepository.findAttendanceInfoByAttendeeId(
                attendeeId);

        int attendance = 0;
        int absence = 0;

        for(AttendeeAttendDetailInfo attendDetailInfo : attendanceInfoByAttendeeId){
            if(attendDetailInfo.getAttendanceStatus()){
                attendance++;
            }else{
                absence++;
            }
        }

        return AttendeeDetailDto.builder()
                .attendee(attendee)
                .attendance(attendance)
                .absence(absence)
                .attendanceInfo(attendanceInfoByAttendeeId)
                .build();
    }

    @Override
    public DuplicateAttendees getDuplicatesAll(Long courseId, List<String> newAttendees) {
        Set<String> uniqueAttendees = new HashSet<>();
        Set<String> duplicated = new HashSet<>();
        DuplicateAttendees duplicateAttendees = new DuplicateAttendees(new LinkedList<>());

        newAttendees.stream()
                .filter(newAttendee -> {
                    if(!uniqueAttendees.add(newAttendee)){
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

    @Override
    public DuplicateAttendees getAlreadyExists(Long courseId, List<String> newAttendees,
                                               DuplicateAttendees duplicateAttendees) {
        List<Attendee> attendeesByCourseId = attendeeRepository.findAttendeesByCourse_Id(courseId);

        Set<String> attendeesAlreadyExists = Set.copyOf(newAttendees);

        attendeesByCourseId.stream()
                .filter(attendee -> attendeesAlreadyExists.contains(attendee.getName()))
                .forEach(attendee -> duplicateAttendees.addDuplicateAttendee(attendee.getId(),attendee.getName(), attendee.getNote()));

        return duplicateAttendees;
    }

    @Override
    public List<SessionAttendees> getSessionAttendeesIfExistsOrEmpty(Long sessionId) {
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionAttendees(sessionId);

        if(sessionAttendees == null || sessionAttendees.isEmpty())
            throw new SessionAttendeesEmptyException();

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                .attendanceId(sessionAttendee.getAttendanceId())
                .name(sessionAttendee.getAttendeeName())
                .note(sessionAttendee.getAttendeeNote())
                .attendanceStatus(sessionAttendee.getAttendanceStatus())
                .attendanceTime(sessionAttendee.getAttendanceTime())
                .build()).toList();
    }

    @Override
    public List<SessionAttendees> getSessionAbsenteesIfExistsOrEmpty(Long sessionId) {
        List<SessionAttendeeInfo> sessionAttendees = attendeeRepository.findSessionOnlyAbsentee(sessionId);

        if(sessionAttendees == null || sessionAttendees.isEmpty())
            throw new SessionAttendeesEmptyException();

        return sessionAttendees.stream().map(sessionAttendee -> SessionAttendees.builder()
                .attendanceId(sessionAttendee.getAttendanceId())
                .name(sessionAttendee.getAttendeeName())
                .note(sessionAttendee.getAttendeeNote())
                .attendanceStatus(sessionAttendee.getAttendanceStatus())
                .attendanceTime(sessionAttendee.getAttendanceTime())
                .build()
        ).toList();
    }

    @Override
    public List<ClassAttendees> getClassAttendeesIfExistsOrEmpty(Long courseId) {
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

    @Override
    public int getAllByCourseId(Long courseId) {
        return attendeeRepository.findAttendeesByCourse_Id(courseId).size();
    }
}
