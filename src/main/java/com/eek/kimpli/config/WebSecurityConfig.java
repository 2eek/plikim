package com.eek.kimpli.config;

import com.eek.kimpli.hellogreeting.service.YourService;
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
    final YourService yourService;


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/check-login").authenticated() // "/check-login"은 인증된 사용자에게만 허용

                //누구나 접근 가능
                .antMatchers("/", "/account/register", "/css/**", "/js/**", "/api/**", "/img/**", "/static/**", "/user/memberjoin").permitAll()
                //로그인이 필요함. 인증 필요!
                //.anyRequest().authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/user/loginForm") //매핑uri
                .permitAll()
                .loginProcessingUrl("/login") // 스프링 시큐리티에서 처리하기위한 주소. /login post방식.
                .usernameParameter("userId") // 이 부분을 추가
                .defaultSuccessUrl("/")
                .and()
                .sessionManagement()
                .maximumSessions(1) // 최대 세션 수
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/user/loginForm") // 세션이 만료된 경우 이동할 URL
                .and()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    if (authentication != null) {
                        // 현재 로그인한 사용자의 이름 가져오기
                        String loggedInUsername = authentication.getName();

                        // 웹 소켓 이벤트 발송
                        yourService.handleUserLogout(loggedInUsername);
                        System.out.println("로그아웃회원아이디????"+loggedInUsername);

                        // ... 기타 로그아웃 관련 로직 ...
                    } else {
                        // 사용자가 로그인되어 있지 않은 상태에서 로그아웃을 시도한 경우
                        // ... 로그아웃 핸들러에 필요한 추가 로직 ...
                    }

                    // "/"로 강제 리다이렉트
                    response.sendRedirect("/");
                })
                .invalidateHttpSession(true);
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //jdbc이용
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                //인증처리 (로그인처리)Authentication
                .usersByUsernameQuery("select user_id, password, enabled from user where user_id = ? and deleted = 0")
                //인가처리 (사용자 권한) Authorization
                .authoritiesByUsernameQuery("select u.user_id, r.name " +
                        "from user_role ur " +
                        "inner join user u on ur.user_index = u.user_index " +
                        "inner join role r on ur.role_index = r.index " +
                        "where u.user_id = ?");

    }


}