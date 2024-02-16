package com.waruru.areyouhere.user.service;

import com.waruru.areyouhere.user.domain.entity.Manager;
import com.waruru.areyouhere.user.domain.repository.UserRepository;
import com.waruru.areyouhere.user.exception.DuplicatedEmailException;
import com.waruru.areyouhere.user.exception.MemberNotFoundException;
import com.waruru.areyouhere.user.exception.UnAuthenticatedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionUserService implements UserService {

    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String LOG_ID = "logId";

    @Override
    public boolean login(Manager manager) {
        Manager findManager = userRepository.findUserByEmail(manager.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if(passwordEncoder.matches(manager.getPassword(), findManager.getPassword())){
            httpSession.setAttribute(LOG_ID, manager.getId());
            return true;
        }
        return false;
    }
    @Override
    public void logout() {
        httpSession.removeAttribute(LOG_ID);
    }

    @Override
    public void register(Manager manager){
        boolean isEmailDuplicated = isDuplicatedEmail(manager.getEmail());

        if(isEmailDuplicated){
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        userRepository.save(manager);
        httpSession.setAttribute(LOG_ID, manager.getId());
    }

    @Override
    public Manager getLoginedUser(){
        Long userId = (Long) httpSession.getAttribute(LOG_ID);
        if (userId == null){
            throw new UnAuthenticatedException();
        }

        return userRepository.findUserById(userId).orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public boolean isDuplicatedEmail(String email){
        return userRepository.existsByEmail(email);
    }
}
