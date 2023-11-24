package com.eek.kimpli.config;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomUsernamePasswordAuthenticationFilter() {
        super();
        setUsernameParameter("userId"); // 사용자명 파라미터를 userId로 설정
    }

    // 다른 설정이나 커스텀 로직이 필요한 경우 여기에 추가할 수 있음
}
