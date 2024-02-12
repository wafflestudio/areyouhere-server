package com.waruru.areyouhere.user.service;

import com.waruru.areyouhere.user.domain.entity.User;

public interface UserService {

    public boolean login(User user);

    public void register(User user);

    public void logout();

    public User getLoginedUser();

    public boolean isDuplicatedEmail(String email);
}
