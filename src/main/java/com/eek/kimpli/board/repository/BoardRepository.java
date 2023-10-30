package com.eek.kimpli.board.repository;

import com.eek.kimpli.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long > {

    List<Board> findByTitle(String title);
    List<Board> findByTitleOrContent(String title, String content);

    //검색기능
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
}
