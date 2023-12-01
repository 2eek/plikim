package com.eek.kimpli.comment.model;

import com.eek.kimpli.replycomment.model.ReplyComment;
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

    //여기서 mappedBy = "commentId"는 ReplyComment 엔터티에서 Comment 엔터티를 참조하는 필드가 commentId임을 나타냅니다.
    // childComments: 부모 댓글에 속한 대댓글들을 리스트로 저장하는 용도 mappedBy = "commentId"
//관계의 주인이 되는 엔터티(ReplyComment)의 필드(commentId)를 지정
@OneToMany(mappedBy = "commentId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<ReplyComment> childComments;
}
