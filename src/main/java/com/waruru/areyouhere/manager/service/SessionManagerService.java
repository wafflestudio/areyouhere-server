package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.auth.entity.LoginUser;
import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.email.domain.MessageTemplate;
import com.waruru.areyouhere.email.service.EmailService;
import com.waruru.areyouhere.course.domain.repository.CourseRepository;
import com.waruru.areyouhere.course.service.CourseService;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.manager.domain.repository.VerifyCodeRepository;
import com.waruru.areyouhere.manager.exception.DuplicatedEmailException;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionManagerService implements ManagerService {
    // TODO: refactor: 계층 구조 위반
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final ManagerRepository managerRepository;
    private final SessionManager sessionManager;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final VerifyCodeRepository verifyCodeRepository;

    private static final String SIGNUP_EMAIL_TITLE = "이메일 인증";
    private static final String PASSWORD_RESET_EMAIL_TITLE = "비밀번호 재설정";


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
    public void signUp(String email, String password, String nickname){
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

    @Override
    @Transactional
    public void delete(Long userId){
        courseRepository.findAllByManagerId(userId).forEach(course -> courseService.delete(userId, course.getId()));
        managerRepository.deleteById(userId);
    }
    public void checkEmailForSignUp(String email){
        String verificationCode = verifyCodeRepository.saveAndGetCode(email);
        emailService.sendVerifyEmail(email, SIGNUP_EMAIL_TITLE, verificationCode, MessageTemplate.SIGN_UP);
    }

    public void checkEmailForPasswordReset(String email){
        String verificationCode = verifyCodeRepository.saveAndGetCode(email);
        emailService.sendVerifyEmail(email, PASSWORD_RESET_EMAIL_TITLE, verificationCode, MessageTemplate.PASSWORD_RESET);
    }

}
