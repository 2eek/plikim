package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.replycomment.model.ReplyComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    // 댓글 저장
    String saveOrUpdateComment(Comment comment);

    // 댓글 페이징 처리 조회
    Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable);

    // 대댓글 저장
    String saveOrUpdateReplyComment(ReplyComment replyComment, Long parentCommentId);

    // 댓글 ID를 이용하여 대댓글 조회
    List<ReplyComment> findByParentComment(Long commentId);



    // 댓글과 대댓글을 함께 가져오는 메서드(댓글아이디에 해당하는 최신순 대댓글)
     List<ReplyComment> getReplyWithCommentId(Long commentId);


}
