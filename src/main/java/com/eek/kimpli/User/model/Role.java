package com.eek.kimpli.User.model;


import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;//권한이름
    @ManyToMany(mappedBy = "roles")
//    @JsonIgnore

private List<User> users = new ArrayList<>();
}
