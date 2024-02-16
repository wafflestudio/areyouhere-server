package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.manager.domain.entity.Manager;

public interface ManagerService {

    public boolean login(Manager manager);

    public void register(Manager manager);

    public void logout();

    public Manager getLoginedUser();

    public boolean isDuplicatedEmail(String email);
}
