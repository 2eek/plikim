package com.eek.kimpli.replycoment.model;

import com.eek.kimpli.comment.model.Comment;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class ReplyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 대댓글 자체의 번호
    private String commentWriter;
    private String commentContents;


    @JoinColumn(name = "board_id")
    private Long boardId; // 게시글 번호

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment; // 부모 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReplyComment> childComments; // 대댓글 목록

    private LocalDateTime commentCreatedTime; // 대댓글 생성 시간
}
