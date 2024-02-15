package com.waruru.areyouhere.user.service;

import com.waruru.areyouhere.user.domain.entity.User;
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
    public boolean login(User user) {
        User findUser = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(MemberNotFoundException::new);

        if(passwordEncoder.matches(user.getPassword(), findUser.getPassword())){
            httpSession.setAttribute(LOG_ID, user.getId());
            return true;
        }
        return false;
    }
    @Override
    public void logout() {
        httpSession.removeAttribute(LOG_ID);
    }

    @Override
    public void register(User user){
        boolean isEmailDuplicated = isDuplicatedEmail(user.getEmail());

        if(isEmailDuplicated){
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        userRepository.save(user);
        httpSession.setAttribute(LOG_ID, user.getId());
    }

    @Override
    public User getLoginedUser(){
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
