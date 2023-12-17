package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
public interface UserFileRepository extends JpaRepository<User, Long> {

    // userId를 기준으로 이미지 정보 업데이트
@Transactional
@Modifying
@Query("UPDATE User u SET " +
       "u.originProfileImg = :#{#user.originProfileImg}, " +
       "u.storedFileName = :#{#user.storedFileName}, " +
       "u.fileAttached = :#{#user.fileAttached} " +
       "WHERE u.userId = :#{#user.userId}")
int updateMyProfileImgByUserId(@Param("user") User user);
}