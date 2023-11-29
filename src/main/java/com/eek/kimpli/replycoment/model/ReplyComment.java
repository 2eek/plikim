package com.eek.kimpli.replycoment.model;

import com.eek.kimpli.comment.model.Comment;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ReplyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대댓글 자체의 번호

    private String replyCommentWriter; // 대댓글 작성자
    private String replyCommentContents; // 대댓글 내용

    @JoinColumn(name = "board_id")
    private Long boardId; // 게시글 번호


    @JoinColumn(name = "parent_comment_id")
    private Long parentComment;



    @OneToMany(mappedBy = "parentComment")
private List<ReplyComment> childComments = new ArrayList<>();

    private LocalDateTime commentCreatedTime; // 대댓글 생성 시간

    private byte deleted; // 소프트 딜리트
}


