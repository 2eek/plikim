package com.eek.kimpli.moment.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Data
public class Moment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Size(min = 2, max = 30, message = "제목은 2자 이상 30자 이하입니다.")
//    private String title;

    @NotNull
    private String content;

    @NotNull
    private String author;

    @Min(value = 0)
    private int views;

    @NotNull
    private LocalDateTime createdDate; // 등록일

    private LocalDateTime updatedDate; // 수정일

    private LocalDateTime deletedDate; // 삭제일

    // 생성자
    public Moment() {
        this.views = 0;
        this.createdDate = LocalDateTime.now();
    }


    // 세터 등도 필요에 따라 추가할 수 있음
}