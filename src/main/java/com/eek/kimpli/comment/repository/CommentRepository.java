package com.eek.kimpli.comment.repository;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.replycoment.model.ReplyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
    //게시글에 해당하는 댓글 조회
//    List<Comment> findByBoardId(Long boardId);
    //페이징 객체 처리하는 댓글 (게시글 아이디에 해당하는)
    Page<Comment> findByBoardId(Long boardId, Pageable pageable);

    List<ReplyComment> findReplyCommentsById(Long Id);


}
