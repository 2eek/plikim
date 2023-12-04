package com.eek.kimpli.user.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//Entity객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동 증 (IDENTITY사용하면 시퀀스 따로 안만들어도 됨)
    private Long index;

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
    private String loginType;
    private String accessToken;

    //@JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_index"),
            inverseJoinColumns = @JoinColumn(name = "role_index"))
    //private List<Role> roles = new ArrayList<>();
     private List<Role> roles = new ArrayList<>(); //nullPointException 방지

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<Board> boards = new ArrayList<>();

}
