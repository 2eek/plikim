package com.eek.kimpli.replycomment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyCommentDTO {
    private String replyCommentWriter;
    private String replyCommentContents;
    private LocalDateTime replyCommentCreatedTime; // 대댓글 생성 시간

    // 생성자
    public ReplyCommentDTO(String replyCommentWriter, String replyCommentContents, LocalDateTime replyCommentCreatedTime) {
        this.replyCommentWriter = replyCommentWriter;
        this.replyCommentContents = replyCommentContents;
        this.replyCommentCreatedTime = replyCommentCreatedTime;
    }

    // 디폴트 생성자

}
