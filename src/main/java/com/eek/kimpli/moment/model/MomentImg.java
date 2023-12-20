package com.eek.kimpli.moment.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
public class MomentImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moment_id", nullable = false)
    private Moment moment;

    @NotNull
    private String storedFileName;

    @NotNull
    private String originProfileImg;

    private LocalDateTime imgCreatedDate; // 사진 등록일

    private LocalDateTime imgDeletedDate; // 사진 삭제일

    private int displayOrder;

    // 다른 필드들은 사진에 대한 정보 (파일 경로, 파일 이름 등)를 나타낼 수 있습니다.
    // 예: private String filePath;
    // 예: private String fileName;

    // 생성자, 세터 등 필요에 따라 추가할 수 있음
}
