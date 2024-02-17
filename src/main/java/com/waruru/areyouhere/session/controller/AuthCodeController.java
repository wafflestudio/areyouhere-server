package com.waruru.areyouhere.session.controller;


import com.waruru.areyouhere.session.dto.AuthCodeDeactivationRequestDto;
import com.waruru.areyouhere.session.dto.AuthCodeRequestDto;
import com.waruru.areyouhere.session.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthCodeController.AUTH_CODE_API_URL)
public class AuthCodeController {

    public static final String AUTH_CODE_API_URL = "/api/auth-code";

    private final AuthCodeService authCodeService;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody AuthCodeRequestDto authCodeRequestDto){
        return ResponseEntity.ok(authCodeService.createAuthCode(authCodeRequestDto.getCourseId(), authCodeRequestDto.getSessionId()));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<HttpStatus> deactivate(@RequestBody AuthCodeDeactivationRequestDto authCode){
        authCodeService.deactivate(authCode.getAuthCode());
        return ResponseEntity.ok().build();
    }


}
