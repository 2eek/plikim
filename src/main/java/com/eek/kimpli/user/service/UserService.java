package com.eek.kimpli.user.service;

import com.eek.kimpli.user.controller.DuplicateUserDataException;
import com.eek.kimpli.user.model.Role;
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserFileRepository;
import com.eek.kimpli.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFileRepository userFileRepository;

    // 회원가입
@Transactional
    public void save(User user) throws DuplicateUserDataException {
        // 중복된 값 체크
        if (isDuplicateUser(user.getUserId(), user.getPhoneNumber())) {
            throw new DuplicateUserDataException("Duplicate user data");
        }

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
        userRepository.save(user);
    }

    //하나의 계정에 하나의 휴대폰 번호(휴대폰 번호 중복체크)
    private boolean isDuplicateUser(String userId, String phoneNumber) {
        return userRepository.existsByUserIdOrPhoneNumber(userId ,phoneNumber);
    }


//private boolean isValidUsername(String username) {
//    // 유효성 검사를 수행하여 유효한 아이디인지 확인
//    return username != null && username.matches("^[a-zA-Z0-9가-힣]*$") && username.length() >= 5;
//}

    //사용중인 휴대전화인지 체크
    public User findByPhoneNumber(String phoneNumber) {
        // findByPhoneNumber 메서드를 사용하여 사용자 찾기

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
        System.out.println("유저유저유저" + result);
        return result;
    }

    //아이디와 이메일 매칭해서 비밀번호찾기 가능
    public User checkEmailAndUserId(String email, String id) {
        User result = userRepository.findByEmailAndUserId(email, id);
        System.out.println("유저유저유저 이메일과 아이디" + result);
        return result;
    }

    //유효한 이메일인지 확인
     public User checkEmail(String email) {
        User result = userRepository.findByEmail(email);
        System.out.println("유저유저유저 이메일과 아이디" + result);
        return result;
    }

    //회원마이페이지 업데이트
public User updateMyInfo(User user) throws IOException {
    if (user.getProfileFile() != null && !user.getProfileFile().isEmpty()) {
        MultipartFile profileFile = user.getProfileFile();
        String originalFilename = profileFile.getOriginalFilename();
        String storedFilename = System.currentTimeMillis() + "_" + originalFilename;
        String savePath = "Users/2eek/plikim_img/user_profileImg/" + storedFilename;
        profileFile.transferTo(new File(savePath));
        // 파일 업로드 처리가 끝나면 업데이트 로직 호출
        userRepository.updateMyInfo(user);
    }
    return user;
}


}
