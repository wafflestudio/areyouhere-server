package com.waruru.areyouhere.manager.controller;

import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_CONFLICT;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;
import static com.waruru.areyouhere.common.utils.HttpStatusResponseEntity.RESPONSE_OK;

import com.waruru.areyouhere.common.annotation.Login;
import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.dto.request.LoginRequestDto;
import com.waruru.areyouhere.manager.dto.request.ResetPasswordRequestDto;
import com.waruru.areyouhere.manager.dto.request.SignUpRequestDto;
import com.waruru.areyouhere.manager.dto.request.UpdateRequestDto;
import com.waruru.areyouhere.manager.dto.request.VerifyEmailRequestDto;
import com.waruru.areyouhere.manager.dto.response.ManagerDto;
import com.waruru.areyouhere.manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ManagerController.MANAGER_API_URL)
public class ManagerController {

    public static final String MANAGER_API_URL = "/api/auth";
    private final ManagerService managerService;

    @LoginRequired
    @GetMapping("/me")
    public ResponseEntity<ManagerDto> isLogin(@Login Manager manager) {
        return ResponseEntity.ok(ManagerDto.builder()
                .email(manager.getEmail())
                .name(manager.getName())
                .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {

        managerService.signUp(signUpRequestDto.getEmail(), signUpRequestDto.getPassword(), signUpRequestDto.getName());
        return RESPONSE_OK;

    }

    @PostMapping("/login")
    public ResponseEntity<HttpStatus> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        if (managerService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword())) {
            return RESPONSE_OK;
        }

        return RESPONSE_BAD_REQUEST;
    }

    @LoginRequired
    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout() {
        managerService.logout();
        return RESPONSE_OK;
    }

    @GetMapping("/email-availability")
    public ResponseEntity<HttpStatus> isDuplicatedEmail(@RequestParam String email) {
        if (managerService.isDuplicatedEmail(email)) {
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }

    @GetMapping("/unauthorized")
    public ResponseEntity<HttpStatus> unauthorized() {
        return RESPONSE_FORBIDDEN;
    }

    @LoginRequired
    @PutMapping
    public ResponseEntity<HttpStatus> update(@RequestBody UpdateRequestDto updateRequestDto, @Login Manager manager) {
        managerService.update(manager.getId(), updateRequestDto.getName(), updateRequestDto.getPassword());
        return RESPONSE_OK;
    }

    @LoginRequired
    @DeleteMapping
    public ResponseEntity<HttpStatus> delete(@Login Manager manager) {
        managerService.delete(manager.getId());
        return RESPONSE_OK;
    }

    @GetMapping("/email")
    public ResponseEntity<HttpStatus> sendSignUpEmail(@RequestParam String email) {
        managerService.sendEmailForSignUp(email);
        return RESPONSE_OK;
    }

    @GetMapping("/password")
    public ResponseEntity<HttpStatus> sendPasswordEmail(@RequestParam String email) {
        managerService.sendEmailForPasswordReset(email);
        return RESPONSE_OK;
    }

    @PostMapping("/verification")
    public ResponseEntity<HttpStatus> verifyEmail(@RequestBody @Valid VerifyEmailRequestDto verifyEmailRequestDto) {
        managerService.verifyEmail(verifyEmailRequestDto.getEmail(), verifyEmailRequestDto.getCode());
        return RESPONSE_OK;
    }

    @PostMapping("/password")
    public ResponseEntity<HttpStatus> resetPassword(
            @RequestBody @Valid ResetPasswordRequestDto resetPasswordRequestDto) {
        managerService.resetPassword(resetPasswordRequestDto.getEmail(), resetPasswordRequestDto.getPassword());
        return RESPONSE_OK;
    }


}
