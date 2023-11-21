package com.eek.kimpli.User.model;

import lombok.Data;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//Entity객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// 자동 증 (IDENTITY사용하면 시퀀스 따로 안만들어도 됨)
    private Long id;
    //private String userEmail;
    private String username;
    private String password;
    private Boolean enabled;

    //@JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();


//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<Board> boards = new ArrayList<>();

}
