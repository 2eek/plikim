package com.eek.kimpli.moment.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Moment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;


    private String author;


    private LocalDateTime createdDate; // 등록일

    private LocalDateTime updatedDate; // 수정일

    private LocalDateTime deletedDate; // 삭제일

    @OneToMany(mappedBy = "moment", cascade = CascadeType.ALL)
    @OrderBy("displayOrder") // displayOrder 필드를 기준으로 정렬
    private List<MomentImg> momentImgs;

    // 생성자, 세터 등 필요에 따라 추가할 수 있음
}
