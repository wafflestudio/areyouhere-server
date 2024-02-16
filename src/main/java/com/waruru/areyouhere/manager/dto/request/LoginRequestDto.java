package com.waruru.areyouhere.manager.dto.request;

import com.waruru.areyouhere.manager.domain.entity.Manager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginRequestDto {

    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotEmpty
    private String password;

    public static Manager toEntity(LoginRequestDto loginRequestDto, PasswordEncoder passwordEncoder){
        return Manager.builder()
                .email(loginRequestDto.getEmail())
                .password(passwordEncoder.encode(loginRequestDto.getPassword()))
                .build();
    }



}
