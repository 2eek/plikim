package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    String saveOrUpdateComment(Comment comment);
    List<Comment> getCommentsByBoardId(Long boardId);
    Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable);
}
