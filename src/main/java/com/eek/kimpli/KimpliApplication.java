package com.eek.kimpli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@ServletComponentScan
public class KimpliApplication {

	public static void main(String[] args) {
		SpringApplication.run(KimpliApplication.class, args);
	}

}
