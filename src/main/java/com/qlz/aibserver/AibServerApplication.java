package com.qlz.aibserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.qlz.aibserver.mapper")
public class AibServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AibServerApplication.class, args);
	}

}
