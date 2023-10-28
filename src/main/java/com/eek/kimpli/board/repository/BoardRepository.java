package com.eek.kimpli.board.repository;

import com.eek.kimpli.board.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long > {
}
