package com.waruru.areyouhere.attendance.service;

import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.session.domain.entity.Session;
import com.waruru.areyouhere.session.service.dto.AuthCodeInfo;
import java.time.LocalDateTime;

public interface AuthCodeService {
    public AuthCodeInfo checkAuthCodeAndGetSessionId(String authCode, String attendanceName);


    public String createAuthCode(Course course, Session sessionId, LocalDateTime currentTime);

    public void deactivate(String authCode);
}
