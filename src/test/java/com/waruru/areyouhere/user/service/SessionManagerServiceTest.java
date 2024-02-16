package com.waruru.areyouhere.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.waruru.areyouhere.user.domain.entity.Manager;
import com.waruru.areyouhere.user.domain.repository.UserRepository;
import com.waruru.areyouhere.user.dto.request.LoginRequestDto;
import com.waruru.areyouhere.user.dto.request.SignUpRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SessionManagerServiceTest {
    @InjectMocks
    private SessionUserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private SignUpRequestDto signUpRequestDto;

    private LoginRequestDto loginRequestDto;

    private Manager manager;

    @BeforeEach
    void setUp(){
        when(passwordEncoder.encode(any())).thenReturn("mockPassword123");
        signUpRequestDto = new SignUpRequestDto("test123@naver.com", "test123123", "testman");
        loginRequestDto = new LoginRequestDto("test123@naver.com", "123123");
        manager = SignUpRequestDto.toEntity(signUpRequestDto, passwordEncoder);
    }

    @Test
    @DisplayName("이메일이 중복일 경우")
    void isDuplicatedEmail(){
        //given
        when(userRepository.existsByEmail(any())).thenReturn(false);
        //then
        assertFalse(userService.isDuplicatedEmail(manager.getEmail()));
    }

    @Test
    @DisplayName("이메일이 중복이 아닐 경우")
    void isNotDuplicatedEmail(){
        //given
        when(userRepository.existsByEmail(any())).thenReturn(true);
        //then
        assertTrue(userService.isDuplicatedEmail(manager.getEmail()));
    }




}