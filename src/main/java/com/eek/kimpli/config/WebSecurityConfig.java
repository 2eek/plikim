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
                .antMatchers("/", "/account/register", "/wss/**", "/css/**", "/api/**", "/img/**", "/static/**", "/member/memberjoin").permitAll()
                .anyRequest().authenticated() // 다른 모든 요청은 인증을 필요로 함
            .and()
            .formLogin()
                .loginPage("/member/loginForm")
                .permitAll()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/member/loginForm")
                .sessionFixation().migrateSession()
                .maximumSessions(1).expiredUrl("/login?maxSessions")
            .and()
            .and()
            .logout()
                .logoutUrl("/logout") // 로그아웃 URL 설정
                .permitAll()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                .logoutSuccessUrl("/member/loginForm"); // 로그아웃 성공 후 이동할 페이지
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
