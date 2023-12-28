package com.eek.kimpli.user.config;

import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class UserInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public UserInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String userId = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println("유저1 " + userId);

            User user = userService.getUserById(userId);
            request.setAttribute("commonUser", user);

            // 세션에 사용자 정보 설정
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUserId", userId);
        }
        return true;
    }


}

