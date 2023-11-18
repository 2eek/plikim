package com.eek.kimpli.User.session;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final SessionRegistry sessionRegistry; // 필드 추가

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 정보를 로드하는 코드
        // 여기서는 임의로 User를 반환하도록 예제를 작성하였습니다.
        return User.withUsername(username)
                .password("password")
                .roles("USER")
                .build();
    }

    public void expireUserSessions(String username) {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        for (Object principal : allPrincipals) {
            if (principal instanceof User) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(username)) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}
