package com.waruru.areyouhere.user.dto.request;


import com.waruru.areyouhere.user.domain.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
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
public class SignUpRequestDto {

    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @NotEmpty
    @Pattern(message = "비밀 번호는 영문과 특수 문자, 숫자를 포함하여 8자 이상이어야 합니다.",
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$")
    private String password;

    @NotEmpty
    @Pattern(message = "닉네임은 2자 이상 16자 이하의 영어, 숫자, 한글로 입력해주세요. 초성은 허가하지 않습니다.",
            regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$")
    private String nickname;

    public static User toEntity(SignUpRequestDto signUpRequestDto, PasswordEncoder passwordEncoder){
        return User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .nickname(signUpRequestDto.nickname)
                .build();
    }
}
