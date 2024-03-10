package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.manager.domain.entity.Manager;

public interface ManagerService {

    public boolean login(String email, String password);

    public void register(String email, String password, String nickname);

    public void logout();

    public Manager getLoginUser();

    public boolean isDuplicatedEmail(String email);
}
