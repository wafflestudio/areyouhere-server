package com.waruru.areyouhere.attendance.dto;

import lombok.Data;

@Data
public class AttendRequestDto {
    private String attendeeName;

    private String authCode;
}
