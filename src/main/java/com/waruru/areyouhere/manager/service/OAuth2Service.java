package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.auth.session.SessionManager;
import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import com.waruru.areyouhere.manager.domain.repository.VerifyCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {
    private final ManagerRepository managerRepository;
    private final ManagerService managerService;
    private final SessionManager sessionManager;
    private final VerifyCodeRepository verifyCodeRepository;
    private final RandomIdentifierGenerator randomIdentifierGenerator;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        managerRepository.findManagerByEmail(email)
                .ifPresentOrElse(
                        manager -> {
                            sessionManager.createSession(manager.getId());
                        },
                        () -> {
                            String password = randomIdentifierGenerator.generateRandomIdentifier(10);
                            verifyCodeRepository.saveAndGetCode(email);
                            managerService.signUp(email, password, name);
                        }
                );

        return oAuth2User;
    }
}
