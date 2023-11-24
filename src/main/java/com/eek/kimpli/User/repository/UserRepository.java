package com.eek.kimpli.User.repository;

import com.eek.kimpli.User.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


      @Query(value = "SELECT * FROM user ORDER BY RAND() LIMIT 3", nativeQuery = true)
      List<User> findRandomUsers();
}