package com.waruru.areyouhere.manager.service;

import com.waruru.areyouhere.auth.entity.LoginUser;
import com.waruru.areyouhere.common.utils.random.RandomIdentifierGenerator;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.domain.repository.ManagerRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static com.waruru.areyouhere.auth.session.SessionManager.LOG_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {
    private final ManagerRepository managerRepository;
    private final HttpSession httpSession;
    private final RandomIdentifierGenerator randomIdentifierGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        managerRepository.findManagerByEmail(email)
                .ifPresentOrElse(
                        manager -> {
                            LoginUser loginUser = new LoginUser(manager.getId());
                            httpSession.setAttribute(LOG_ID, loginUser);
                        },
                        () -> {
                            String password = randomIdentifierGenerator.generateRandomIdentifier(10);
                            Manager manager = managerRepository.save(
                                    Manager.builder()
                                            .email(email)
                                            .name(name)
                                            .password(passwordEncoder.encode(password))
                                            .build()
                            );
                            LoginUser loginUser = new LoginUser(manager.getId());
                            httpSession.setAttribute(LOG_ID, loginUser);
                        }
                );

        return oAuth2User;
    }
}
