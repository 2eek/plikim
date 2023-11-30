package com.eek.kimpli.comment.model;

import com.eek.kimpli.replycoment.model.ReplyComment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글자체의 번호
    private String commentWriter;
    private String commentContents;
    private Long boardId; //게시글 번호
    private LocalDateTime commentCreatedTime; //댓글 생성 시간
    private byte deleted;

   @OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ReplyComment> childComments; // 대댓글 목록
}
