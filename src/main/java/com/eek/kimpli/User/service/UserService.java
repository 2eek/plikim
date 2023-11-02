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
        System.out.println(user.getPassword());
       user.setPassword(encodedPassword);
       user.setEnabled(true);
        System.out.println(user.getPassword());
       Role role = new Role();
        System.out.println(role);
        System.out.println("되라");
         System.out.println(role.getUsers());
       role.setId(1L);
        System.out.println(role.getId());
        System.out.println(role);
        System.out.println("test111");
       user.getRoles().add(role);
        System.out.println("test222");
        return userRepository.save(user);
    }
}
