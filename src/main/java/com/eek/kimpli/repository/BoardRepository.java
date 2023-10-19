package com.eek.kimpli.repository;

import com.eek.kimpli.entity.BoardEntity;
import com.eek.kimpli.service.BoardService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long >{
}
