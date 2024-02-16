package com.waruru.areyouhere.attendance.service;


import com.waruru.areyouhere.attendance.domain.entity.Attendance;
import com.waruru.areyouhere.attendance.domain.repository.AttendanceRepository;
import com.waruru.areyouhere.attendance.service.dto.AttendanceCount;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public AttendanceCount getAttendanceCount(long sessionId) {
        List<Attendance> attendancesBySessionId = attendanceRepository.findAttendancesBySession_Id(sessionId);

        if(attendancesBySessionId.isEmpty()){
            return new AttendanceCount(0, 0);
        }
        int attendanceCount = 0;
        int absenceCount = 0;

        for (Attendance attendance : attendancesBySessionId) {
            if(attendance.isAttended()){
                attendanceCount++;
            }else{
                absenceCount++;
            }
        }

        return new AttendanceCount(attendanceCount, absenceCount);
    }

    @Transactional
    public void setAbsent(long sessionId){

    }

}
