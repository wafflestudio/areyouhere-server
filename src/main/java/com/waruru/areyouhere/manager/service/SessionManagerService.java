package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.auth.entity.LoginUser;
import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.course.domain.entity.Course;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.exception.UnauthorizedManagerException;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.MemberNotFoundException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionManagerService implements ManagerService {

    private final ManagerRepository managerRepository;
    private final SessionManager sessionManager;

    @Override
    @Transactional
    public boolean login(Manager manager) {
        Manager findManager = managerRepository.findManagerByEmail(manager.getEmail())
                .orElseThrow(UnAuthenticatedException::new);
        if(manager.getPassword().equals(findManager.getPassword())){
            sessionManager.createSession(findManager.getId());
            return true;
        }
        return false;
    }
    @Override
    @Transactional
    public void logout() {
        sessionManager.removeSession();
    }

    @Override
    @Transactional
    public void register(Manager manager){
        boolean isEmailDuplicated = isDuplicatedEmail(manager.getEmail());

        if(isEmailDuplicated){
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        managerRepository.save(manager);

        sessionManager.createSession(manager.getId());
    }

    @Override
    @Transactional
    public Manager getLoginUser(){
        LoginUser loginUser = sessionManager.getSession();

        if (loginUser == null){
            throw new UnAuthenticatedException();
        }

        return managerRepository.findById(loginUser.getId()).orElseThrow(UnAuthenticatedException::new);
    }

    @Override
    @Transactional
    public boolean isDuplicatedEmail(String email){
        return managerRepository.existsByEmail(email);
    }
}
