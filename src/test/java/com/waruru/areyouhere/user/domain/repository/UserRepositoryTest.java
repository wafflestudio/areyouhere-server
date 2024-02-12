package com.waruru.areyouhere.user.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.waruru.areyouhere.user.domain.entity.User;
import com.waruru.areyouhere.user.dto.SignUpDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class UserRepositoryTest {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    @Transactional
//    @DisplayName("User 저장 테스트")
//    void saveUser(){
//        User user = SignUpDto.toEntity(new SignUpDto("hi98@naver.com", "hello123123"), passwordEncoder);
//
//        User savedUser = userRepository.save(user);
//
//        assertThat(user).isSameAs(savedUser);
//        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
//        assertThat(user.getId()).isNotNull();
//        assertThat(userRepository.count()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("User 조회 테스트")
//    @Transactional
//    void findMember(){
//        User user1 = SignUpDto.toEntity(new SignUpDto("hi98@naver.com", "hello123123"), passwordEncoder);
//        User user2 = SignUpDto.toEntity(new SignUpDto("bye98@naver.com", "bye123123"), passwordEncoder);
//        userRepository.save(user1);
//        userRepository.save(user2);
//
//        User findUser1 = userRepository.findById(user1.getId())
//                .orElseThrow(() -> new IllegalArgumentException());
//        User findUser2 = userRepository.findById(user2.getId())
//                .orElseThrow(() -> new IllegalArgumentException());
//
//        assertThat(userRepository.count()).isEqualTo(2);
//        assertThat(findUser1.getEmail()).isEqualTo(user1.getEmail());
//        assertThat(findUser2.getEmail()).isEqualTo(user2.getEmail());
//    }

}