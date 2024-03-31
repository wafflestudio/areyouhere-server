package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.auth.entity.LoginUser;
import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionManagerService implements ManagerService {

    private final ManagerRepository managerRepository;
    private final SessionManager sessionManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public boolean login(String email, String password) {
        Manager findManager = managerRepository.findManagerByEmail(email)
                .orElseThrow(UnAuthenticatedException::new);
        if(passwordEncoder.matches(password, findManager.getPassword())){
            sessionManager.createSession(findManager.getId());
            return true;
        }
        return false;
    }

    @Override
    public void logout() {
        sessionManager.removeSession();
    }

    @Override
    @Transactional
    public void register(String email, String password, String nickname){
        boolean isEmailDuplicated = isDuplicatedEmail(email);

        if(isEmailDuplicated){
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        Manager manager = managerRepository.save(
                Manager.builder()
                        .name(nickname)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .build()

        );

        sessionManager.createSession(manager.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Manager getLoginUser(){
        LoginUser loginUser = sessionManager.getSession();

        if (loginUser == null){
            throw new UnAuthenticatedException();
        }

        return managerRepository.findById(loginUser.getId()).orElseThrow(UnAuthenticatedException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email){
        return managerRepository.existsByEmail(email);
    }


    @Override
    @Transactional
    public void update(Long userId, String name, String password){
        Manager manager = managerRepository.findById(userId).orElseThrow(UnAuthenticatedException::new);
        managerRepository.save(manager.update(name, passwordEncoder.encode(password)));
    }

    public void delete(Long userId){
        
        managerRepository.deleteById(userId);
    }
}
