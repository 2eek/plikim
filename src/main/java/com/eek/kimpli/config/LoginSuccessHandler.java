//package com.eek.kimpli.config;
//
//import com.eek.kimpli.User.session.CustomUserDetailsService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class LoginSuccessHandler implements AuthenticationSuccessHandler {
//
//    private final CustomUserDetailsService customUserDetailsService; // 수정된 부분
//
//    public LoginSuccessHandler(CustomUserDetailsService customUserDetailsService) { // 수정된 부분
//        this.customUserDetailsService = customUserDetailsService; // 수정된 부분
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        expireUserSessions(authentication);
//        response.sendRedirect("/");
//    }
//
//    private void expireUserSessions(Authentication authentication) {
//        if (authentication instanceof UsernamePasswordAuthenticationToken) {
//            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
//            String username = token.getName();
//            customUserDetailsService.expireUserSessions(username); // 수정된 부분
//            System.out.println("세션 만료 처리 완료");
//        }
//    }
//}
