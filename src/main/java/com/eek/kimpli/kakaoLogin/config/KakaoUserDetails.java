package com.eek.kimpli.kakaoLogin.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class KakaoUserDetails implements UserDetails {

    private final String username;

    public KakaoUserDetails(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한이 필요하다면 부여, 예를 들어 "ROLE_USER"
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        // 사용자의 비밀번호가 있다면 반환, 아니면 null
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되지 않았는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨 있지 않았는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명이 만료되지 않았는지 확인하는 로직
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사용자가 활성화되었는지 확인하는 로직
        return true;
    }
}
