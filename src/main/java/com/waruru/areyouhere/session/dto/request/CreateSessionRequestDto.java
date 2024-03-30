package com.waruru.areyouhere.session.dto.request;

import lombok.Data;

@Data
public class CreateSessionRequestDto {
    Long courseId;
    String sessionName;
}
