package com.waruru.areyouhere.session.dto;

import lombok.Data;

@Data
public class CreateSessionRequestDto {
    Long courseId;
    String sessionName;
}
