package com.waruru.areyouhere.attendance.service.rdb;


import com.waruru.areyouhere.active.domain.entity.AttendInfo;
import com.waruru.areyouhere.active.domain.entity.CurrentSessionAttendanceInfo;
import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceBatchRepository;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.dto.AttendeeRedisData;
import com.waruru.areyouhere.attendance.dto.UpdateAttendance;
import com.waruru.areyouhere.session.exception.SessionIdNotFoundException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttendanceRDBServiceImpl implements AttendanceRDBService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceBatchRepository attendanceBatchRepository;

    @Override
    public void setAttendancesAfterDeactivate(long courseId, long sessionId,
                                              CurrentSessionAttendanceInfo currentSessionAttendanceInfo) {
        LocalDateTime absentTime = LocalDateTime.now();
        List<AttendeeRedisData> attendees = new LinkedList<>();
        List<AttendeeRedisData> absentees = new LinkedList<>();
        Map<Long, AttendInfo> attendanceInfo = currentSessionAttendanceInfo.getAttendInfos();
        currentSessionAttendanceInfo.getAttendees()
                .forEach(att -> {
                    if (attendanceInfo.containsKey(att.getId())) {
                        attendees.add(att);
                    } else {
                        absentees.add(att);
                    }
                });


        attendanceBatchRepository.insertAttendBatch(attendees, true, sessionId, attendanceInfo);
        attendanceBatchRepository.insertAbsentBatch(absentees, false, sessionId, absentTime);
    }


    public void setAttendanceStatuses(List<UpdateAttendance> updateAttendances) {

        List<Attendance> attendancesToUpdate = updateAttendances.stream()
                .map(updateAttendance -> {
                    Attendance attendance = attendanceRepository.findById(updateAttendance.getAttendanceId())
                            .orElseThrow(SessionIdNotFoundException::new);
                    attendance.setStatus(updateAttendance.getAttendanceStatus());

                    return attendance;
                })
                .collect(Collectors.toList());

        attendanceRepository.saveAll(attendancesToUpdate);
    }

    @Override
    public long getSessionId(Long attendanceId){
        return  attendanceRepository.getSessionIdByAttendanceId(attendanceId).orElseThrow();
    }


}
