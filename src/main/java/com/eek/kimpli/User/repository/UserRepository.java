package com.eek.kimpli.User.repository;

import com.eek.kimpli.User.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u ORDER BY RAND()")
    Page<User> findRandomUsers(Pageable pageable);

}
