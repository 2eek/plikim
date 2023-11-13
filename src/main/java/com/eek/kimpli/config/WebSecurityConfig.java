package com.eek.kimpli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;

@Override
protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
            // 누구나 접근 가능한 URL 패턴
            .antMatchers("/", "/account/register", "/css/**", "/api/**", "/img/**", "/static/**", "/member/memberjoin").permitAll()
            .anyRequest().permitAll() // 다른 요청은 모두 허용
            .and()
        .formLogin()
            .loginPage("/member/loginForm")
            .permitAll()
            .loginProcessingUrl("/login") // 스프링 시큐리티에서 처리하기 위한 주소. /login post 방식.
            .defaultSuccessUrl("/")
            .and()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 정책 설정
            .invalidSessionUrl("/member/loginForm") // 세션 만료 시 이동할 URL 설정
            .sessionFixation().migrateSession() // 세션 고정 보호 설정
            .maximumSessions(1).expiredUrl("/login?maxSessions") // 동시 로그인 세션 수 제한 설정
        .and()
        .and()
        .logout()
            .permitAll();
}

@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .passwordEncoder(passwordEncoder)
        .usersByUsernameQuery("select username, password, enabled "
                + "from user "
                + "where username = ?")
        .authoritiesByUsernameQuery("select u.username, r.name "
                + "from user_role ur inner join user u on ur.user_id = u.id "
                + "inner join role r on ur.role_id = r.id "
                + "where u.username = ?");
}




}