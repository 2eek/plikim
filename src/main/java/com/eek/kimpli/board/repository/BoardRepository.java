package com.eek.kimpli.board.repository;

import com.eek.kimpli.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long >{
}
