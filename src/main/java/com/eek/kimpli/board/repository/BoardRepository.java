package com.eek.kimpli.board.repository;

import com.eek.kimpli.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

// 스프링 데이터 JPA는 JpaRepository를 상속받는 인터페이스에 대해 자동으로 빈(bean)을 생성하고 레포지토리(repository)로 인식한다.
public interface BoardRepository extends JpaRepository<Board, Long > { // <모델클래스, pk>
    //제목으로 찾기
    List<Board> findByTitle(String title);
    //제목이나 내용으로 찾기
    //SELECT * FROM Board b WHERE b.title = :title OR b.content = :content;2
    List<Board> findByTitleOrContent(String title, String content);

    //검색기능 + 페이징 적용
    //Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
Page<Board> findByTitleContainingOrContentContainingOrAuthorContaining(String title, String content, String author, Pageable pageable);


    @Modifying
    @Query("UPDATE Board b SET b.views = b.views + 1 WHERE b.id = :boardId")
    void incrementViews(@Param("boardId") Long boardId);
}
