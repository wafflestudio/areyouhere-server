package com.waruru.areyouhere.auth.interceptor;


import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.user.domain.entity.User;
import com.waruru.areyouhere.user.exception.UnAuthenticatedException;
import com.waruru.areyouhere.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            User user = userService.getLoginedUser();

            if (user == null) {
                throw new UnAuthenticatedException();
            }
        }
        return true;
    }


}
