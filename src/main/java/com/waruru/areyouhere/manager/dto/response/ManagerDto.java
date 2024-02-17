package com.waruru.areyouhere.manager.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagerDto {

    private String email;

    private String name;

    public ManagerDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
