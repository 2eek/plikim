package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CommentService {
    String saveOrUpdateComment(Comment comment);
    List<Comment> getCommentsByBoardId(Long boardId);

}