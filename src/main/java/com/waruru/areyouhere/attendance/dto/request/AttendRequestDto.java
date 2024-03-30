package com.waruru.areyouhere.attendance.dto.request;

import lombok.Data;

@Data
public class AttendRequestDto {
    private String attendeeName;

    private Long attendeeId;
    private String authCode;
}
