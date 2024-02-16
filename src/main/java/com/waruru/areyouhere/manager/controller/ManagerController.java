package com.waruru.areyouhere.manager.controller;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_OK;

import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.dto.request.LoginRequestDto;
import com.waruru.areyouhere.manager.dto.request.SignUpRequestDto;
import com.waruru.areyouhere.manager.service.ManagerService;
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
@RequestMapping(ManagerController.MEMBER_API_URL)
public class ManagerController {

    public static final String MEMBER_API_URL = "/api/user";

    private final ManagerService managerService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto){

        managerService.register(SignUpRequestDto.toEntity(signUpRequestDto, passwordEncoder));
        return RESPONSE_OK;

    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginRequestDto loginRequestDto){

        if(managerService.login(LoginRequestDto.toEntity(loginRequestDto, passwordEncoder))){
            return RESPONSE_OK;
        }

        return RESPONSE_BAD_REQUEST;
    }

    @LoginRequired
    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout(){
        managerService.logout();
        return RESPONSE_OK;
    }

    @GetMapping("/{email}")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@PathVariable String email){
        if(managerService.isDuplicatedEmail(email)){
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }


}
