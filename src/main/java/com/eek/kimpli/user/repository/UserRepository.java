package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    User findByEmailAndUserId(String email, String userId);
    User findByEmail(String email);

    // 카카오 로그인 정보 확인
    @Query(value = "SELECT * FROM user WHERE memberEmail = :email AND login_type = 'L2'", nativeQuery = true)
    HashMap<String, Object> findKakaoByEmail(String email);


    //비밀번호 변경
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("newPassword") String newPassword);

//회원가입시 유효성검사

     boolean existsByUserIdOrPhoneNumber(String userId,String phoneNumber);

@Modifying
@Transactional
@Query(value = "UPDATE user " +
       "SET user_name = COALESCE(:#{#user.userName}, user_name), " +
       "phone_number = COALESCE(:#{#user.phoneNumber}, phone_number), " +
               "email = COALESCE(:#{#user.email}, email), " +
       "origin_profile_img = COALESCE(:#{#user.originProfileImg}, origin_profile_img), " +
       "stored_file_name = COALESCE(:#{#user.storedFileName}, stored_file_name), " +
       "file_attached = COALESCE(:#{#user.fileAttached}, file_attached) " +
       "WHERE user_id = :#{#user.userId}", nativeQuery = true)
int updateUserInfo(@Param("user") User user);
}


