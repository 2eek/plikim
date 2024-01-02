package com.eek.kimpli.kakaoLogin.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class KakaoAuthentication implements Authentication {

    private final KakaoUserDetails userDetails;
    private boolean authenticated;
    //KakaoAuthentication 객체를 생성 KakaoUserDetails 객체를 받아들여 사용자의 세부 정보를 나타냄
    //롬복 라이브러리 RequiredArgsConstructor -> 생성자 자동 주입됨
    public KakaoAuthentication(KakaoUserDetails userDetails) {
        //클래스의 멤버 변수 userDetails에 KakaoUserDetails 객체를 할당, 인스턴스(userDetails)를 참조
        //KakaoUserDetails 객체를 userDetails 변수에 저장함
        //KakaoAuthentication 클래스의 다른 메서드에서 해당 객체에 접근하여 사용할 수 있음 this.userDetails에서의 userDetails에 저장됨
        this.userDetails = userDetails;
        this.authenticated = true; // 인증 상태를 true로 설정
    }

    @Override
    //다양한 유형의 권한 객체들을 모두 수용
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        // 사용자 이름을 반환
        return userDetails.getUsername();
    }
}
