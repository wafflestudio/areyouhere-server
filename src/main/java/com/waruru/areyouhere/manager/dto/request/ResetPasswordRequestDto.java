package com.waruru.areyouhere.manager.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ResetPasswordRequestDto {

    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    String email;

    @NotEmpty
    @Pattern(message = "비밀 번호는 영문과 특수 문자, 숫자를 포함하여 8자 이상이어야 합니다.",
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,20}$")
    String password;
}
