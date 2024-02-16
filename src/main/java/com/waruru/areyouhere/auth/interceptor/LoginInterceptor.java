package com.waruru.areyouhere.auth.interceptor;


import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import com.waruru.areyouhere.manager.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final ManagerService managerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            Manager manager = managerService.getLoginedUser();

            if (manager == null) {
                throw new UnAuthenticatedException();
            }
        }
        return true;
    }


}
