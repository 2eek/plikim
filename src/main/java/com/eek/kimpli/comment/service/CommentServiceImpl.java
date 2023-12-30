package com.eek.kimpli.comment.service;

import com.eek.kimpli.board.model.Board;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.comment.repository.CommentRepository;
import com.eek.kimpli.replycomment.model.ReplyComment;
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

    // 댓글 저장
    @Override
    public String saveOrUpdateComment(Comment comment) {
        comment.setCommentCreatedTime(LocalDateTime.now());
        commentRepository.save(comment);
        return "success";
    }

    // 댓글 페이징 조회
    @Override
    public Page<Comment> getCommentsByBoardId(Long boardId, Pageable pageable) {
        return commentRepository.findByBoardIdOrderByCommentCreatedTimeDesc(boardId, pageable);
    }

    // 대댓글 저장
    @Override
    public String saveOrUpdateReplyComment(ReplyComment replyComment, Long parentCommentId) {
        Comment parentCommentOb = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        // 댓글과 대댓글 설정
        parentCommentOb.getChildComments().add(replyComment);
        replyComment.setCommentId(parentCommentOb.getId());

        // 댓글과 대댓글 저장
        commentRepository.save(parentCommentOb);

        return "success";
    }

    // 댓글 ID를 이용하여 대댓글 조회
    @Override
    public List<ReplyComment> findByParentComment(Long commentId) {
        commentRepository.findReplyCommentsById(commentId);
        System.out.println("레파지토리test" + commentRepository.findReplyCommentsById(commentId));
        return commentRepository.findReplyCommentsById(commentId);
    }

    @Override
    public List<ReplyComment> getReplyWithCommentId(Long commentId) {
        System.out.println("서비스 임플" + commentId);
        List<ReplyComment> a = commentRepository.findReplyComments(commentId);
        System.out.println("서비스 임플 대댓글 리스트" + a);
        return commentRepository.findReplyComments(commentId);
    }

    @Override
    public Page<Comment> getCommentsByCommentWriter(String commentWriter, Pageable pageable) {
        return commentRepository.findByCommentWriter(commentWriter, pageable);
    }
}