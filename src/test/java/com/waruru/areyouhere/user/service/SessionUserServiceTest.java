package com.waruru.areyouhere.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.waruru.areyouhere.user.domain.entity.User;
import com.waruru.areyouhere.user.domain.repository.UserRepository;
import com.waruru.areyouhere.user.dto.LoginDto;
import com.waruru.areyouhere.user.dto.SignUpDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SessionUserServiceTest {
    @InjectMocks
    private SessionUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private SignUpDto signUpDto;

    private LoginDto loginDto;

    private User user;

    @BeforeEach
    void setUp(){
        when(passwordEncoder.encode(any())).thenReturn("mockPassword123");
        signUpDto = new SignUpDto("test123@naver.com", "test123123");
        loginDto = new LoginDto("test123@naver.com", "123123");
        user = SignUpDto.toEntity(signUpDto, passwordEncoder);
    }

    @Test
    @DisplayName("이메일이 중복일 경우")
    void isDuplicatedEmail(){
        //given
        when(userRepository.existsByEmail(any())).thenReturn(false);
        //then
        assertFalse(userService.isDuplicatedEmail(user.getEmail()));
    }

    @Test
    @DisplayName("이메일이 중복이 아닐 경우")
    void isNotDuplicatedEmail(){
        //given
        when(userRepository.existsByEmail(any())).thenReturn(true);
        //then
        assertTrue(userService.isDuplicatedEmail(user.getEmail()));
    }




}