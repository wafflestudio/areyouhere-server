package com.waruru.areyouhere.session.dto.request;

import com.waruru.areyouhere.session.service.dto.UpdateSession;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateSessionsRequestDto {
    private List<UpdateSession> sessions;
}



