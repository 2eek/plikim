package com.eek.kimpli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"com.eek.kimpli.comment.converter", "com.eek.kimpli.comment.model"})
@SpringBootApplication
public class KimpliApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimpliApplication.class, args);

	}

}
