//package com.eek.kimpli.config;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        // 세션을 직접 무효화하는 로직 추가
//        request.getSession().invalidate();
//        response.sendRedirect("/member/loginForm");
//    }
//}
//
