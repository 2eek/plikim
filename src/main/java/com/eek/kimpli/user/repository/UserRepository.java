package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    User findByUserIdAndDeleted(String userId, int deleted);

    //랜덤으로 회원리스트 나열
    @Query(value = "SELECT * FROM user WHERE deleted = 0 ORDER BY RAND() LIMIT 3 ", nativeQuery = true)
    List<User> findRandomUsers();


    //회원의 휴대폰 번호 변경
    @Modifying
    @Query("UPDATE User u SET u.phoneNumber = :newPhoneNumber WHERE u.userId = :userId")
    int updateUserPhoneNumber(@Param("newPhoneNumber") String newPhoneNumber, @Param("userId") String userId);

    //회원의 이메일 변경
    @Modifying
    @Query("UPDATE User u SET u.email = :newEmail WHERE u.userId = :userId")
    int updateEmail(@Param("newEmail") String Email, @Param("userId") String Id);//Param의 키값으로 쿼리문에서 시행됨

    // 휴대폰 번호로 사용자 찾기
    User findByPhoneNumberAndDeleted(String phoneNumber, int deleted);

    //이메일로 비밀번호 찾기
    User findByEmailAndUserId(String email, String userId);

    //이메일 유무 확인
    User findByEmail(String email);

    // 카카오 로그인 정보 확인
//    @Query(value = "SELECT * FROM user WHERE email = :email AND login_type = 'L2'", nativeQuery = true)
//    HashMap<String, Object> findKakaoByEmail(String email);
    //삭제안된 회원들 리스트 조회
    @Query("SELECT u FROM User u WHERE u.deleted = 0")
    Page<User> findUndeletedUser(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.deleted = 0 AND u.phoneNumber = :phoneNumber")
    User findUndeletedUserOne(@Param("phoneNumber") String phoneNumber);


    //비밀번호 변경
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userId = :userId")
    int updatePasswordByEmail(@Param("userId") String userId, @Param("newPassword") String newPassword);

    //mypage 비밀번호 변경
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.userId = :Id")
    int updatePasswordById(@Param("Id") String userId, @Param("newPassword") String newPassword);
//회원가입시 유효성검사

    @Query("SELECT u.userId, u.phoneNumber FROM User u " +
            "WHERE u.userId = :userId OR " +
            "(u.phoneNumber = :phoneNumber AND u.phoneNumber IN (SELECT phoneNumber FROM User WHERE deleted = 0))")
    List<User> findDuplicateUsers(@Param("userId") String userId, @Param("phoneNumber") String phoneNumber);

    //회원정보 업데이트
//@Modifying
//@Transactional
//@Query(value = "UPDATE user " +
//       "SET phone_number = COALESCE(:#{#user.phoneNumber}, phone_number), " +
//       "email = CASE WHEN :#{#user.loginType} = 'L2' THEN email ELSE COALESCE(:#{#user.email}, email) END, " +
//       "origin_profile_img = COALESCE(:#{#user.originProfileImg}, origin_profile_img), " +
//       "stored_file_name = COALESCE(:#{#user.storedFileName}, stored_file_name), " +
//       "file_attached = COALESCE(:#{#user.fileAttached}, file_attached) " +
//       "WHERE user_id = :#{#user.userId}", nativeQuery = true)
//int updateUserInfo(@Param("user") User user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user " +
            "SET origin_profile_img = COALESCE(:#{#user.originProfileImg}, origin_profile_img), " +
            "stored_file_name = COALESCE(:#{#user.storedFileName}, stored_file_name), " +
            "file_attached = COALESCE(:#{#user.fileAttached}, file_attached) " +
            "WHERE user_id = :#{#user.userId}", nativeQuery = true)
    int updateUserProfile(@Param("user") User user);


    //회원탈퇴. 소프트딜리트
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deletedDate = CURRENT_TIMESTAMP, u.deleted = 1 WHERE u.deleted = 0 AND u.userId = :userId AND u.phoneNumber = :phoneNumber")
    int withdraw(@Param("phoneNumber") String phoneNumber, @Param("userId") String userId);
}


