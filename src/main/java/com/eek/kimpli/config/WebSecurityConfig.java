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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final DataSource dataSource;
    final PasswordEncoder passwordEncoder;
    final UserDetailsService userDetailsService;
    //final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login1", "/account/register", "/css/**", "/js/**", "/api/**", "/img/**", "/static/**", "/member/memberjoin").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/member/loginForm")
                .permitAll()
                .loginProcessingUrl("/login")
//                .successHandler(loginSuccessHandler) // 커스텀 핸들러 등록
                .and()
            .sessionManagement()
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/member/loginForm")
                .and()
                .and()
            .logout()
                .logoutSuccessUrl("/member/loginForm")  /* 로그아웃 성공시 이동할 url */
                .invalidateHttpSession(true)  /*로그아웃시 세션 제거*/
                .deleteCookies("JSESSIONID")  /*쿠키 제거*/
                .clearAuthentication(true)    /*권한정보 제거*/
                .permitAll()
                .and()
            .sessionManagement()
                .maximumSessions(1) /* session 허용 갯수 */
                .expiredUrl("/login") /* session 만료시 이동 페이지*/
                .maxSessionsPreventsLogin(false); /* 동일한 사용자 로그인시 x, false 일 경우 기존 사용자 session 종료*/
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
