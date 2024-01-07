package com.eek.kimpli.user.service;

import com.eek.kimpli.user.controller.DuplicateUserDataException;
import com.eek.kimpli.user.model.Role;
import com.eek.kimpli.user.model.User;
import com.eek.kimpli.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    @Value("${external.upload.path}")
    private String path;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public int save(User user) throws DuplicateUserDataException {
        // 중복된 값 체크
        if (!isDuplicateUser(user.getUserId(), user.getPhoneNumber()).isEmpty()) {
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
        user.setDeleted(0);

        // 사용자 정보 저장
           User result = userRepository.save(user);
       if(result != null){
           return 1;
       }else{
           return 0;
       }
    }

    //하나의 계정에 하나의 휴대폰 번호(휴대폰 번호 중복체크)
    private List<User> isDuplicateUser(String userId, String phoneNumber) {
        System.out.println("중복결과" + userRepository.findDuplicateUsers(userId, phoneNumber));
        return userRepository.findDuplicateUsers(userId, phoneNumber);
    }

    //사용중인 휴대전화인지 체크
    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumberAndDeleted(phoneNumber, 0);
    }


    //비밀번호 찾기 중 비밀번호 수정하기
    public int editPassword(String userId, String password) {
        User user =getUserById(userId);
         if (passwordEncoder.matches(password, user.getPassword())) {
            return -1;
        } else{
        String newPw = passwordEncoder.encode(password);
        // 비밀번호 업데이트 메서드 호출
        return userRepository.updatePasswordByEmail(userId,newPw);
    }}

    //마이페이지 비밀번호 수정
    public int updatePassword(String userId, String password) {
        // 새로 변경된 비밀번호 암호화
        User user = userRepository.findByUserId(userId);
        //새로 입력한 비밀번호와 기존 비밀번호가 같다면
        if (passwordEncoder.matches(password, user.getPassword())) {
            return -1;
        } else {
            user.setPassword(passwordEncoder.encode(password));
            // 비밀번호 업데이트 메서드 호출
            return userRepository.updatePasswordById(user.getUserId(), user.getPassword());
        }
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
        return result;
    }



    public int updatePhoneNumber(String phoneNumber, String userId) {
        return userRepository.updateUserPhoneNumber(phoneNumber, userId);

    }
//email업데이트
    public int updateEmail(String email, String userId) {
        return userRepository.updateEmail(email, userId);

    }

    public User getUserById(String userId) {
        // userRepository를 사용하여 userId를 기반으로 사용자 정보를 가져옴
        return userRepository.findByUserId(userId);
    }


    //회원 사진 업데이트
    public int updateProfile(String id, MultipartFile profileFile) throws IOException {
        System.out.println("?????" + id + profileFile);
        User user = userRepository.findByUserId(id);
        System.out.println("서비스 유저" + user);
        System.out.println("유저 정보?" + user.getEmail());
        if (profileFile != null && !profileFile.isEmpty()) {
            user.setOriginProfileImg(profileFile.getOriginalFilename());
            user.setStoredFileName(System.currentTimeMillis() + "_" + profileFile.getOriginalFilename());
            user.setFileAttached(1);
            String savePath = path + user.getStoredFileName();
            FileService.saveFile(profileFile.getBytes(), savePath);
            userRepository.updateUserProfile(user); //수정필요
            return 1;
        } else {
          return 0;
        }

    }





    //회원탈퇴. 소프트 딜리트
    public int withdraw(String phoneNumber,String userId) {
        return userRepository.withdraw(phoneNumber, userId);
    }
}


