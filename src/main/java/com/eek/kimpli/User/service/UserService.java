package com.eek.kimpli.User.service;


import com.eek.kimpli.User.model.Role;
import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;



    //회원가입
    public User save(User user){
       String encodedPassword = passwordEncoder.encode(user.getPassword());
       user.setPassword(encodedPassword);
       user.setEnabled(true);
       Role role = new Role();
      // 권한 이름에  1 주기
       role.setIndex(1L);  //어떤 권한 줄건지 1번이 'ROLE_user'
       user.getRoles().add(role);
       user.setCreatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }
}
