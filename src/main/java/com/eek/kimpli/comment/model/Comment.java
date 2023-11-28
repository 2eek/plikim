package com.eek.kimpli.comment.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
}
