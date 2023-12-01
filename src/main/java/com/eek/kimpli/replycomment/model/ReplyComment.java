package com.eek.kimpli.replycomment.model;


import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class ReplyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대댓글 자체의 번호
    private String replyCommentWriter; // 대댓글 작성자
    private String replyCommentContents; // 대댓글 내용
       @JoinColumn(name = "comment_id")
    private Long commentId;

    private LocalDateTime replyCommentCreatedTime; // 대댓글 생성 시간
    private byte deleted; // 소프트 딜리트

    public LocalDateTime getReplyCommentCreatedTime() {
    return this.replyCommentCreatedTime;
}
}


