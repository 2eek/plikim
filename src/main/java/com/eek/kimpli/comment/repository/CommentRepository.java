package com.eek.kimpli.comment.repository;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
     List<Comment> findByBoardId(Long boardId);

}
