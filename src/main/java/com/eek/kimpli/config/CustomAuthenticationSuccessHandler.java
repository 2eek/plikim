//package com.eek.kimpli.config;
//
//import com.eek.kimpli.User.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
////로그인시 세션값에 user 객체 넣기
//@Component
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//
//    @Autowired
//    private HttpSession session;
//
//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication
//    ) throws IOException, ServletException {
//        User user = (User) authentication.getPrincipal();
//        session.setAttribute("user", user); // 세션에 사용자 엔터티 추가
//
//        // 로그인 성공 후의 동작을 추가할 수 있습니다.
//        // ...
//
////        // 기본 로그인 성공 URL로 이동
////        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
//    }
//}
