package com.eek.kimpli.moment.repository;

import com.eek.kimpli.moment.model.Moment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MomentRepository extends JpaRepository<Moment, Long > {

}
