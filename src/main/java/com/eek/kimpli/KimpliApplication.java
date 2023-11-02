package com.eek.kimpli;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.eek.kimpli.**")
//@EntityScan(basePackages = "com.eek.kimpli.**")
//@EnableMongoRepositories
public class KimpliApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimpliApplication.class, args);
	}

}
