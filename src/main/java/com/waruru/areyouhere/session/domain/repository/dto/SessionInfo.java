package com.waruru.areyouhere.session.domain.repository.dto;

import java.time.LocalDateTime;

public interface SessionInfo {

    Long getid();

    LocalDateTime getdate();
    String getname();
    int getattendee();

    int getabsentee();

}
