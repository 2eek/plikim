package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.replycoment.model.ReplyComment;
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


}
