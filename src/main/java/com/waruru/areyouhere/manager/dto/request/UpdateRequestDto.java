package com.waruru.areyouhere.manager.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UpdateRequestDto {
    @NotEmpty
    @Pattern(message = "닉네임은 2자 이상 16자 이하의 영어, 숫자, 한글로 입력해주세요. 초성은 허가하지 않습니다.",
            regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$")
    private final String name;

    @NotEmpty
    @Pattern(message = "비밀 번호는 영문과 특수 문자, 숫자를 포함하여 8자 이상이어야 합니다.",
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,20}$")
    private final String password;
}
