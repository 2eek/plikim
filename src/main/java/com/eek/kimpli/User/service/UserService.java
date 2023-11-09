package com.eek.kimpli.User.service;


import com.eek.kimpli.User.model.Role;
import com.eek.kimpli.User.model.User;
import com.eek.kimpli.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
