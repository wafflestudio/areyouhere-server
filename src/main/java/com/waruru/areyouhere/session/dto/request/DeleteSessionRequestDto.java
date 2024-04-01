package com.waruru.areyouhere.session.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class DeleteSessionRequestDto {
    private List<Long> sessionIds;
}
