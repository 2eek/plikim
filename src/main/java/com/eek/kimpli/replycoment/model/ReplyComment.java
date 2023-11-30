package com.eek.kimpli.replycoment.model;


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


       @JoinColumn(name = "comment_id")
    private Long commentId;





@OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<ReplyComment> childComments = new ArrayList<>();

    private LocalDateTime replyCommentCreatedTime; // 대댓글 생성 시간

    private byte deleted; // 소프트 딜리트
}


