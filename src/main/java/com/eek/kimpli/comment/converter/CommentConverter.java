package com.eek.kimpli.comment.converter;

import com.eek.kimpli.comment.dto.CommentDTO;
import com.eek.kimpli.comment.model.Comment;
import com.eek.kimpli.replycomment.dto.ReplyCommentDTO;
import com.eek.kimpli.replycomment.model.ReplyComment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    public CommentDTO toCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        // Comment에서 CommentDTO로 필드 복사
        commentDTO.setId(comment.getId());
        commentDTO.setCommentWriter(comment.getCommentWriter());
        commentDTO.setCommentContents(comment.getCommentContents());
        commentDTO.setBoardId(comment.getBoardId());
        commentDTO.setCommentCreatedTime(comment.getCommentCreatedTime());

        // ReplyCommentDTO 리스트로 변환
        List<ReplyCommentDTO> replyCommentsDTO = comment.getChildComments().stream()
                .map(this::toReplyCommentDTO)
                .collect(Collectors.toList());
        commentDTO.setChildComments(replyCommentsDTO);

        return commentDTO;
    }

    public ReplyCommentDTO toReplyCommentDTO(ReplyComment replyComment) {
        ReplyCommentDTO replyCommentDTO = new ReplyCommentDTO();
        // ReplyComment에서 ReplyCommentDTO로 필드 복사
        replyCommentDTO.setReplyCommentWriter(replyComment.getReplyCommentWriter());
        replyCommentDTO.setReplyCommentContents(replyComment.getReplyCommentContents());
        replyCommentDTO.setReplyCommentCreatedTime(replyComment.getReplyCommentCreatedTime());

        return replyCommentDTO;
    }
}