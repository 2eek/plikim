package com.eek.kimpli.config;

import com.sun.xml.bind.v2.util.DataSourceSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private DataSource dataSource;



   @Bean//의존성 주입 -> 객체를 사용할 수 있게 된다.
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
				.loginPage("/member/login1")//get방식 action url
				.permitAll()
        )
        .logout(logout ->
			logout
				.permitAll()
			// 로그아웃 요청을 처리할 URL 설정
				.logoutUrl("/logout")
			// 로그아웃 성공 시 리다이렉트할 URL 설정
				.logoutSuccessUrl("/")
			// 로그아웃 핸들러 추가 (세션 무효화 처리)
				.addLogoutHandler((request, response, authentication) -> {
				HttpSession session = request.getSession();
				session.invalidate();
			})
		);
    return http.build();
}
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth)
//			  throws Exception {
//			auth.jdbcAuthentication()
//				  .dataSource(dataSource)
//				  .passwordEncoder(passwordEncoder())// 스프링에서 인스턴스를 빈으로 관리함. 비밀번호 관리화(스프링에서 인증처리 할 때 인코딩 이용해서 알아서 비밀번호 암호화를 한다.)
//					//인증처리
//				  .usersByUsernameQuery("select memberEmail,memberPassword,enabled "
//							+ "from bael_users "
//							+ "where email = ?")
//					//권한처리
//				  .authoritiesByUsernameQuery("select email,authority "
//							+ "from authorities "
//							+ "where email = ?");
//	}
//	//비밀번호 암호화
//	@Bean
//    public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//    }


}