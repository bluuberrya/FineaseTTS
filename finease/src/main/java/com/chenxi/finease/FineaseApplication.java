package com.chenxi.finease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.chenxi.finease")
@EntityScan({"com.chenxi.finease.model"})
@EnableJpaRepositories("com.chenxi.finease.repository")

=======
<<<<<<< HEAD
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chenxi.finease"})
@EntityScan({"com.chenxi.finease.model", "com.chenxi.finease.security"})
@EnableJpaRepositories("com.chenxi.finease.dao")

=======

@SpringBootApplication
>>>>>>> parent of 06fb326 (create fineasedb on mySQL. issue with dao files)
>>>>>>> FineaseTTS/main
public class FineaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FineaseApplication.class, args);
	}

}