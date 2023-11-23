package com.eek.kimpli.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {




    final DataSource dataSource;

    final PasswordEncoder passwordEncoder;


  @Bean
public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
               .csrf().disable()
    .authorizeRequests()
        .antMatchers("/", "/account/register", "/css/**", "/js/**","/api/**", "/img/**", "/static/**", "/member/memberjoin").permitAll()
        .anyRequest().authenticated()
        .and()
    .formLogin()
        .loginPage("/member/loginForm")
        .permitAll()
        .loginProcessingUrl("/login") // 스프링 시큐리티에서 처리하기 위한 주소. /login post 방식.
        .defaultSuccessUrl("/")
        .and()
    .sessionManagement()
        .maximumSessions(1)
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .expiredUrl("/") // 중복 로그인 시 기존 세션 만료 후 이동할 URL
        .and()
    .and()
    .logout()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .logoutSuccessUrl("/")
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