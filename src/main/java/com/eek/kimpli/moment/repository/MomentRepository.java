package com.eek.kimpli.moment.repository;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MomentRepository extends JpaRepository<Moment, Long > {
//Moment의 momentImg 필드를 이용
    @EntityGraph(attributePaths = "momentImg")
    List<Moment> findAll();




}



