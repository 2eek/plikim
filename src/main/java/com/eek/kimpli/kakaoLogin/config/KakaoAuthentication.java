package com.eek.kimpli.kakaoLogin.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class KakaoAuthentication implements Authentication {

    private final KakaoUserDetails userDetails;
    private boolean authenticated;

    public KakaoAuthentication(KakaoUserDetails userDetails) {
        this.userDetails = userDetails;
        this.authenticated = true; // 인증 상태를 true로 설정
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한을 부여할 필요가 있다면 구현
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
