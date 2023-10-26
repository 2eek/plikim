package com.eek.kimpli.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()
        .authorizeRequests(authorizeRequests ->
            authorizeRequests
                .antMatchers("/").permitAll()
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
                .permitAll()
        );
    return http.build();
}



	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}