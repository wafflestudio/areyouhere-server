package com.waruru.areyouhere.session.dto;

import com.waruru.areyouhere.session.service.dto.SessionAttendanceInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreviousFiveSessionResponseDto {
    private List<SessionAttendanceInfo> sessionAttendanceInfos;

    @Builder
    public PreviousFiveSessionResponseDto(List<SessionAttendanceInfo> sessionAttendanceInfos) {
        this.sessionAttendanceInfos = sessionAttendanceInfos;
    }
}
