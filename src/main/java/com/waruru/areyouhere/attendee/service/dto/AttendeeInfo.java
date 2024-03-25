package com.waruru.areyouhere.attendee.service.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendeeInfo {
    private Long id;
    private String name;
    private String note;
    @Builder
    public AttendeeInfo(Long id, String name, String note) {
        this.id = id;
        this.name = name;
        this.note = note;
    }
}
