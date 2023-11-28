package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public String saveOrUpdateComment(Comment comment) {
        comment.setCommentCreatedTime(LocalDateTime.now());
        commentRepository.save(comment);
        return "success";
    }

    @Override
    public List<Comment> getCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

//    @Override
//    public Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable) {
//        return commentRepository.findByBoardId(boardId, pageable);
//    }

    @Override
    public Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable) {
    Page<Comment> commentsPage = commentRepository.findByBoardId(boardId, pageable);

    if (commentsPage == null) {
        // commentsPage가 null이면 빈 페이지 객체를 생성하여 반환
        return Page.empty();
    }

    return commentsPage;
}
}
