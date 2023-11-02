package com.eek.kimpli.tabletest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface Memberrepository1 extends JpaRepository<Member1, Long> {

}