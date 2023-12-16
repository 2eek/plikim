package com.eek.kimpli.user.repository;

import com.eek.kimpli.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFileRepository extends JpaRepository<User, Long> {
}
