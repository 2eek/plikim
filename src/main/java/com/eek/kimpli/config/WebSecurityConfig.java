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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    final DataSource dataSource;
    final PasswordEncoder passwordEncoder;
   //로그인 시 userId 칼럼명 사용
      // 주입 받은 필터를 빈으로 등록
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

  @Bean
public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
      .addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 필터 빈을 호출하여 등록
                .csrf().disable()
                .authorizeRequests()
                        //누구나 접근 가능
                        .antMatchers("/", "/account/register", "/css/**", "/js/**","/api/**" ,"/img/**","/static/**","/member/memberjoin").permitAll()
                        //로그인이 필요함. 인증 필요!
                        //.anyRequest().authenticated()
                .anyRequest().permitAll()
                        .and()
                .formLogin()
                        .loginPage("/member/loginForm") //매핑uri
                        .permitAll()
                       .loginProcessingUrl("/login") // 스프링 시큐리티에서 처리하기위한 주소. /login post방식.
                     .defaultSuccessUrl("/")
                        .and()
                .sessionManagement()
                .maximumSessions(1) // 최대 세션 수
                .sessionRegistry(sessionRegistry())
                .expiredUrl("/member/loginForm") // 세션이 만료된 경우 이동할 URL
                .and()
                .and()
                .logout()
                    .permitAll()
                    .logoutSuccessUrl("/member/loginForm")
                    .invalidateHttpSession(true);



    }

    //로그인 과정에서의 스프링 내부에서 인증수행한다.
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      //jdbc이용
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .passwordEncoder(passwordEncoder)
            //인증처리 (로그인처리)Authentication
        .usersByUsernameQuery("select user_id, password, enabled "
                + "from user "
                + "where user_id = ?")
            //인가처리 (사용자 권한) Authorization
        .authoritiesByUsernameQuery("select u.user_id, r.name "
                + "from user_role ur inner join user u on ur.user_index = u.index "
                + "inner join role r on ur.role_index = r.index "
                + "where u.user_id = ?");

}



}