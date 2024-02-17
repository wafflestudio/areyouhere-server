package com.waruru.areyouhere.session.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllSessionAttendanceInfo {
    private List<SessionAttendanceInfo> allSessionAttendanceInfo;

    @Builder
    public AllSessionAttendanceInfo(List<SessionAttendanceInfo> allSessionAttendanceInfo) {
        this.allSessionAttendanceInfo = allSessionAttendanceInfo;
    }
}
