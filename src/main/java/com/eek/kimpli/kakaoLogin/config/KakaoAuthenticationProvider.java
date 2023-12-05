//package com.eek.kimpli.kakaoLogin.config;
//
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//
//public class KakaoAuthenticationProvider implements AuthenticationProvider {
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // Kakao로부터 전달받은 정보
//        String kakaoUserId = (String) authentication.getPrincipal(); // 카카오에서 받은 사용자 ID
//        // 기타 필요한 정보들...
//
//        // 사용자 정보 조회 및 인증
//        UserDetails userDetails = loadUserByUsername(kakaoUserId);
//
//        if (userDetails == null) {
//            throw new UsernameNotFoundException("Kakao user not found");
//        }
//
//        // 인증이 완료되면 UsernamePasswordAuthenticationToken을 사용하여 Authentication 객체 생성
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }
//
////    @Override
////    public boolean supports(Class<?> authentication) {
////        return authentication.equals(UsernamePasswordAuthenticationToken.class);
////    }
//
//    // Kakao 사용자 정보를 기반으로 UserDetails 객체를 생성하는 메서드
//    private UserDetails loadUserByUsername(String userId) {
//        // 실제로는 데이터베이스 조회 등을 통해 사용자 정보를 가져오는 로직이 들어갑니다.
//        // 이 예시에서는 간단히 UserDetails를 생성하여 반환하도록 합니다.
//        return new User(userId);
//    }
//
//
//
//}
//
