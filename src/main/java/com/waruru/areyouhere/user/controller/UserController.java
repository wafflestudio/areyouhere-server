package com.waruru.areyouhere.user.controller;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_OK;

import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.user.dto.request.LoginRequestDto;
import com.waruru.areyouhere.user.dto.request.SignUpRequestDto;
import com.waruru.areyouhere.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(UserController.MEMBER_API_URL)
public class UserController {

    public static final String MEMBER_API_URL = "/api/user";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){

        userService.register(SignUpRequestDto.toEntity(signUpRequestDto, passwordEncoder));
        return RESPONSE_OK;

    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginRequestDto loginRequestDto){

        if(userService.login(LoginRequestDto.toEntity(loginRequestDto, passwordEncoder))){
            return RESPONSE_OK;
        }

        return RESPONSE_BAD_REQUEST;
    }

    @LoginRequired
    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout(){
        userService.logout();
        return RESPONSE_OK;
    }

    @GetMapping("/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable String email){
        if(userService.isDuplicatedEmail(email)){
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }


}
