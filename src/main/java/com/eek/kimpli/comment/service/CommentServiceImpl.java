package com.eek.kimpli.comment.service;

import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    //댓글 저장
    @Override
    public String saveOrUpdateComment(Comment comment) {
            comment.setCommentCreatedTime(LocalDateTime.now());
             commentRepository.save(comment);
        // 예: commentRepository.save(comment);
        // 성공 또는 실패 여부에 따라 적절한 문자열 반환
        return "success"; // 또는 실패시 메시지를 반환
    }

@Override
public List<Comment> getCommentsByBoardId(Long boardId) {
    List<Comment> comments = commentRepository.findByBoardId(boardId);
    // 로깅 추가
    System.out.println("Comments for Board ID " + boardId + ": " + comments);
    return comments;
}

}

