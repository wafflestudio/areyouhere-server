package com.waruru.areyouhere.attendance.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateAttendStatusDto {
    private String authCode;
    private Long sessionId;
    private Long courseId;
}
