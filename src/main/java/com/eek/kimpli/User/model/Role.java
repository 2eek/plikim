package com.eek.kimpli.User.model;


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
    private Long id;
    private String name;//권한이름
    @ManyToMany(mappedBy = "roles")
//    @JsonIgnore

private List<User> users = new ArrayList<>();
}
