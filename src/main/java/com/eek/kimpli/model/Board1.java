package com.eek.kimpli.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //DB연동을 위한 모델클레스임을 알려줌
@Data
public class Board1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
}

//모델을 사용하기 위한 레파지토리 생성한다.
