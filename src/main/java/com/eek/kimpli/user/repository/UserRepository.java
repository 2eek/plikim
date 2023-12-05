package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        // id로 유저 찾기
        User findByUserId(String userId);

        //랜덤으로 회원리스트 나열
      @Query(value = "SELECT * FROM user ORDER BY RAND() LIMIT 3", nativeQuery = true)
      List<User> findRandomUsers();

        // 이메일로 유저 찾기


    // 휴대폰 번호로 사용자 찾기
    User findByPhoneNumber(String phoneNumber);

    // 카카오 로그인 정보 확인
    @Query(value = "SELECT * FROM user WHERE memberEmail = :email AND login_type = 'L2'", nativeQuery = true)
    HashMap<String, Object> findKakaoByEmail(String email);
}