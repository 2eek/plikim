package com.eek.kimpli.board.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

@Entity //DB연동을 위한 모델클레스임을 알려줌
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min=2, max=30, message = "제목은 2자 이상 30자 이하입니다.") //유효성 검사
    private String title;
    @NotNull
    //@Size(min=2)
    private String content;
    @NotNull
    private String author;
    @Min(value = 0)
    private int views;
    @NotNull
    private Date createdDate; // 등록일

    private Date updatedDate; // 수정일

    private Date deletedDate; // 삭제일

     // 생성자
    public Board() {
        this.views = 0;
        this.createdDate = new Date(System.currentTimeMillis());
    }
}

//모델을 사용하기 위한 레파지토리 생성한다.
