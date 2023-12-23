package com.eek.kimpli.moment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

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

//    @JsonIgnore//JSON 직렬화 시 해당 필드를 무시하도록 지정하는 것으로, 상호참조나 무한루프를 방지하기 위해 사용됨
//    @ToString.Exclude //무한루프 방지 .상호참조
//    @OneToMany(mappedBy = "moment", cascade = CascadeType.ALL)
//    @OrderBy("displayOrder") // displayOrder 필드를 기준으로 정렬
//    private List<MomentImg> momentImg;

@JsonIgnore//JSON 직렬화 시 해당 필드를 무시하도록 지정하는 것으로, 상호참조나 무한루프를 방지하기 위해 사용됨
@ToString.Exclude //무한루프 방지 .상호참조
@OneToMany(mappedBy = "moment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
@OrderBy("displayOrder")
private List<MomentImg> momentImg;







    // 생성자, 세터 등 필요에 따라 추가할 수 있음
}
