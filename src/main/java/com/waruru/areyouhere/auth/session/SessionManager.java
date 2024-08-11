package com.waruru.areyouhere.auth.session;

import com.waruru.areyouhere.auth.entity.LoginUser;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SessionManager {

    private final HttpSession httpSession;

    private static final String LOG_ID = "logId";

    public void createSession(Long managerId){
        LoginUser loginUser = new LoginUser(managerId);
        httpSession.setAttribute(LOG_ID, loginUser);
    }

    public void removeSession(){
        httpSession.removeAttribute(LOG_ID);
    }

    public LoginUser getSession(){
        return (LoginUser) httpSession.getAttribute(LOG_ID);
    }


}
