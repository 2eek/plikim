package com.eek.kimpli.board.model;

import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity //DB연동을 위한 모델클레스임을 알려줌
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min=2, max=30)
    private String title;
     @NotNull
    @Size(min=2)
    private String content;
}

//모델을 사용하기 위한 레파지토리 생성한다.
