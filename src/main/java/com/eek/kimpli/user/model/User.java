package com.eek.kimpli.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Data
@Component
//Entity객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미
public class User {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동 증 (IDENTITY사용하면 시퀀스 따로 안만들어도 됨)
//    private Long index;
    private Long userIndex;
    private String userId; //회원의 아이디
    private String email;
    private String userName;
    private String phoneNumber;
    private String password;
    private Boolean enabled;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime createdDate;

    @Column(columnDefinition = "DATETIME")
    private LocalDateTime deletedDate;
    private Boolean deleted;
    private String loginType;
    private String accessToken;
    private String refreshToken;
    private String gender;


    //첨부파일. 실제 파일 담아줄 수 있음
    @Lob
    @Transient
    private MultipartFile profileFile;  //MultipartFile 타입으로 담아와서 컨트롤러로 넘겨준다. 파일음 담는 용도
    private String originProfileImg; //원본 파일 이름
    private String storedFileName; //서버 저장용 파일 이름(중복 이름 구분하기 위해)
    private int fileAttached; //파일 첨부 여부(첨부1 미첨부0)

 // 파일 정보만 저장
//    private String originalFilename;
//    private String storedFilename;




    //@JsonIgnore
    @JsonIgnore
    @ToString.Exclude //무한루프 방지 .상호참조
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_index"),
            inverseJoinColumns = @JoinColumn(name = "role_index"))

     private List<Role> roles = new ArrayList<>(); //nullPointException 방지


}
