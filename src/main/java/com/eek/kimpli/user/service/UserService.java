package com.eek.kimpli.user.service;

import com.eek.kimpli.user.model.Role;
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public User save(User user) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);

        // 권한 설정
        Role role = new Role();
        role.setIndex(1L);  // 'ROLE_user'에 해당하는 권한 번호
        user.getRoles().add(role);

        // 가입일 설정
        user.setCreatedDate(LocalDateTime.now());

        // 사용자 정보 저장
        return userRepository.save(user);
    }

    // 추가: 휴대폰 번호로 사용자 찾기
//    public User findByPhoneNumber(String phoneNumber) {
//        return userRepository.findByPhoneNumber(phoneNumber);
//    }

    public User findByPhoneNumber(String phoneNumber) {
        // findByPhoneNumber 메서드를 사용하여 사용자 찾기
        System.out.println("xxxxxxx");
        System.out.println("번호없냐?"+phoneNumber);
        User user = userRepository.findByPhoneNumber(phoneNumber);
    System.out.println("yyyyyyy");
        // 사용자 정보 출력 또는 다른 작업 수행
        if (user != null) {
            System.out.println("사용자 찾음: " + user);
        } else {
            System.out.println("사용자 없음");
        }
        return user;
    }


	 //비밀번호 찾기 중 비밀번호 수정하기
    	 public int editPassword(User user) {
        // 새로 변경된 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 비밀번호 업데이트 메서드 호출
        return userRepository.updatePasswordByEmail(user.getEmail(), user.getPassword());
    }


  //회원가입시 아이디 중복체크

     public User checkId(String userId) {
   User result = userRepository.findByUserId(userId);
         System.out.println("유저유저유저"+result);
    return result;
}
}

