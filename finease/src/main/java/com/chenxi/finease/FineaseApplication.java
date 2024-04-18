package com.chenxi.finease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chenxi.finease"})
@EntityScan({"com.chenxi.finease.model", "com.chenxi.finease.security"})
@EnableJpaRepositories("com.chenxi.finease.dao")

public class FineaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FineaseApplication.class, args);
	}

}