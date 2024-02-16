package com.waruru.areyouhere.session.dto;


import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentFiveSessionResponseDto {


}

class PreviousSession{
    private LocalDateTime date;
    private String sessionName;

    private Long attendance;

    private Long absence;
}