package com.eek.kimpli.comment.repository;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.replycomment.model.ReplyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long > {
    //게시글에 해당하는 댓글 조회
//    List<Comment> findByBoardId(Long boardId);
    //페이징 객체 처리하는 댓글 (게시글 아이디에 해당하는)

   Page<Comment> findByBoardIdOrderByCommentCreatedTimeDesc(Long boardId, Pageable pageable);


    List<ReplyComment> findReplyCommentsById(Long Id);

    //댓글을 이용해 대댓글을 최신순으로 get

    @Query("SELECT rc FROM ReplyComment rc WHERE rc.commentId =  :commentId ORDER BY rc.replyCommentCreatedTime DESC ")
    List<ReplyComment> findReplyComments(@Param("commentId") Long commentId);

    //댓글 soft delete
    @Modifying
    @Query("UPDATE Comment c SET c.deleted = 1 WHERE c.id = :commentId")
    void softDeleteComment(@Param("commentId") Long commentId);

Page<Comment> findByCommentWriter(String commentWriter, Pageable pageable);



}