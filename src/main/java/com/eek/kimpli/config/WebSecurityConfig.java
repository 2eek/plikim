package com.eek.kimpli.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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
                        //누구나 접근 가능
                        .antMatchers("/", "/account/register", "/css/**", "/api/**" ,"/img/**","/static/**","/member/memberjoin").permitAll()
                        //로그인이 필요함. 인증 필요!
                        //.anyRequest().authenticated()
                .anyRequest().permitAll()
                        .and()
                .formLogin()
                        .loginPage("/member/loginForm")
                        .permitAll()
                       .loginProcessingUrl("/login") // 스프링 시큐리티에서 처리하기위한 주소. /login post방식.
                     .defaultSuccessUrl("/")
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