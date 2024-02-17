package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.MemberNotFoundException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionManagerService implements ManagerService {

    private final HttpSession httpSession;
    private final ManagerRepository managerRepository;


    private static final String LOG_ID = "logId";

    @Override
    @Transactional
    public boolean login(Manager manager) {
        Manager findManager = managerRepository.findManagerByEmail(manager.getEmail())
                .orElseThrow(MemberNotFoundException::new);
        if(manager.getPassword().equals(findManager.getPassword())){
            httpSession.setAttribute(LOG_ID, manager.getId());
            return true;
        }
        return false;
    }
    @Override
    @Transactional
    public void logout() {
        httpSession.removeAttribute(LOG_ID);
    }

    @Override
    @Transactional
    public void register(Manager manager){
        boolean isEmailDuplicated = isDuplicatedEmail(manager.getEmail());

        if(isEmailDuplicated){
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        managerRepository.save(manager);
        httpSession.setAttribute(LOG_ID, manager.getId());
    }

    @Override
    @Transactional
    public Manager getLoginedUser(){
        Long userId = (Long) httpSession.getAttribute(LOG_ID);

        if (userId == null){
            throw new UnAuthenticatedException();
        }

        return managerRepository.findById(userId).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional
    public boolean isDuplicatedEmail(String email){
        return managerRepository.existsByEmail(email);
    }
}
