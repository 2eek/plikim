package com.eek.kimpli.moment.repository;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MomentRepository extends JpaRepository<Moment, Long > {
//Moment의 momentImg 필드를 이용
   @EntityGraph(attributePaths = "momentImg")
    @Query("SELECT m FROM Moment m ORDER BY m.createdDate DESC")  // 여기에 정렬을 추가
    List<Moment> findAll();


   List<Moment> findAllByAuthorOrderByCreatedDateDesc(String Author);


}



