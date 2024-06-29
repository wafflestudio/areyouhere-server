package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.manager.domain.entity.Manager;

public interface ManagerService {

    public boolean login(String email, String password);

    public void signUp(String email, String password, String nickname);

    public void logout();

    public Manager getLoginUser();

    public boolean isDuplicatedEmail(String email);

    public void update(Long userId, String name, String password);

    public void delete(Long userId);

    public void sendEmailForSignUp(String email);

    public void sendEmailForPasswordReset(String email);

    public void verifyEmail(String email, String verificationCode);

    public void resetPassword(String email, String password);
}
