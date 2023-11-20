package com.eek.kimpli.User.service;


import com.eek.kimpli.User.model.Role;
import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
       role.setId(1L);
       user.getRoles().add(role);

        return userRepository.save(user);
    }
}
