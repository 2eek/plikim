package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        User findByUserId(String userId);

      @Query(value = "SELECT * FROM user ORDER BY RAND() LIMIT 3", nativeQuery = true)
      List<User> findRandomUsers();
}