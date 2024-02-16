package com.waruru.areyouhere.user.service;

import com.waruru.areyouhere.user.domain.entity.Manager;

public interface UserService {

    public boolean login(Manager manager);

    public void register(Manager manager);

    public void logout();

    public Manager getLoginedUser();

    public boolean isDuplicatedEmail(String email);
}
