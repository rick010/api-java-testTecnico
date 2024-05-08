package com.dev.ricardo.apitestuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class ApiTestUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTestUserApplication.class, args);
	}

}
