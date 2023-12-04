package com.eek.kimpli.user.model;


import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//데이터 베이스 연동을 위한 모델클래스
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long index;
    private String name;//권한이름
    @ManyToMany(mappedBy = "roles") //mappedBy는 User클래스에 있는 roles
//private List<User> users = new ArrayList<>();
    private List<User> users = new ArrayList<>(); ///nullPointException 방지
}
