package com.waruru.areyouhere.auth.interceptor;


import com.waruru.areyouhere.common.annotation.LoginRequired;
import com.waruru.areyouhere.manager.domain.entity.Manager;
import com.waruru.areyouhere.manager.exception.UnAuthenticatedException;
import com.waruru.areyouhere.manager.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.swing.DebugGraphics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final ManagerService managerService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginRequired.class)) {
            try {
                Manager manager = managerService.getLoginUser();
                //TODO CSRF 토큰 도입 혹은 JWT 도입 - 프론트엔드와 합의 필요
                // TODO referer 체크
                // TODO 세션을 유지한다면 장기적으로 redis로 넘어갈텐데 SecurityContextRepository를 사용해야할 것 같다.

            } catch (UnAuthenticatedException e) {
                request.getRequestDispatcher("/api/manager/unauthorized").forward(request, response);
                return false;
            }
        }
        return true;
    }


}
