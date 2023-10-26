package com.eek.kimpli.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	 //비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()
        .authorizeRequests(authorizeRequests ->
            authorizeRequests
                .antMatchers("/").permitAll()
				//저장소에 Authentication없으면 로그인폼으로 돌림
                .antMatchers("/board/**").authenticated()
                .antMatchers("/static/**").permitAll()
                .anyRequest().permitAll()
        )
        .formLogin(formLogin ->
            formLogin
				.loginPage("/member/loginForm")
                .permitAll()
        )
        .logout(logout ->
			logout
			// 로그아웃 요청을 처리할 URL 설정
			.logoutUrl("/logout")
			// 로그아웃 성공 시 리다이렉트할 URL 설정
			.logoutSuccessUrl("/")
//			// 로그아웃 핸들러 추가 (세션 무효화 처리)
			.addLogoutHandler((request, response, authentication) -> {
				HttpSession session = request.getSession();
				session.invalidate();
			})
//			// 로그아웃 성공 핸들러 추가 (리다이렉션 처리)
//			.logoutSuccessHandler((request, response, authentication) ->
//					response.sendRedirect("/"))
//			// 로그아웃 시 쿠키 삭제 설정 (예: "remember-me" 쿠키 삭제)
//			.deleteCookies("remember-me")
                );

    return http.build();
}



//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
}